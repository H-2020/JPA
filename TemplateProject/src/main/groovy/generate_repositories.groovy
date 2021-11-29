import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

db_name = "tbbdb"
db_user = "root"
db_pwd = "pass"
packageName = "be.intec.repositories"
entityPackageName = packageName.replaceAll(".repositories", ".models.entities")
dataBinderPackageName = packageName.replaceAll(".repositories", ".models.binders")
exceptionsPackageName = packageName.replaceAll(".repositories", ".services.exceptions")

typeMapping = [
        (~/(?i)boolean/)            : "Boolean",
        (~/(?i)int/)                : "Integer",
        (~/(?i)long/)               : "Long",
        (~/(?i)blob|longblob|binary|bfile|clob|raw|image/)      : "java.sql.Blob",
        (~/(?i)float/)              : "Float",
        (~/(?i)double|decimal|real/): "Double",
        (~/(?i)datetime|timestamp/) : "java.sql.Timestamp",
        (~/(?i)date/)               : "java.sql.Date",
        (~/(?i)time/)               : "java.sql.Time",
        (~/(?i)/)                   : "String"
]

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable }.each { generateJavasFile(it, dir) }
}

def generateJavasFile(table, dir) {
    def className = javaName(table.getName(), true)
    def fields = calcFields(table)
    def connectionFactoryFile = new File(dir, "ConnectionFactory.java");
    if (!connectionFactoryFile.exists()) {
        connectionFactoryFile.withPrintWriter { out -> generateConnectionFactorySourceCode(out) }
    }
    new File(dir, className + "Repository.java").withPrintWriter { out -> generateSourceCode(out, className, fields) }
}

def generateConnectionFactorySourceCode(out) {

    out.println "package ${packageName};"
    out.println ""
    out.println "import java.sql.*;"
    out.println ""
    out.println "public class ConnectionFactory {"
    out.println "    String connectionUrl = \"jdbc:mysql://localhost:3306/${db_name}\";"
    out.println "    String dbUser = \"${db_user}\";"
    out.println "    String dbPwd = \"${db_pwd}\";"
    out.println ""
    out.println "    private static ConnectionFactory connectionFactory = null;"
    out.println ""
    out.println "    private ConnectionFactory() { }"
    out.println ""
    out.println "    public Connection getConnection() throws SQLException {"
    out.println "        Connection conn = null;"
    out.println "        conn = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);"
    out.println "        return conn;"
    out.println "    }"
    out.println ""
    out.println "    public static ConnectionFactory getInstance() {"
    out.println "        if (connectionFactory == null) {"
    out.println "            connectionFactory = new ConnectionFactory();"
    out.println "        }"
    out.println "        return connectionFactory;"
    out.println "    }"
    out.println "}"
}

def generateSourceCode(out, className, fields) {
    out.println "package $packageName;"
    out.println ""
    out.println "import $entityPackageName.*;"
    out.println "import java.sql.*;"
    out.println "import java.util.*;"
    out.println "import $exceptionsPackageName.*;"
    out.println ""
    out.println "public class ${className}Repository {"
    out.println ""
    out.println "    private Connection connection = null;"
    out.println "    private PreparedStatement statement = null;"
    out.println "    private ResultSet results = null;"
    out.println "    "
    out.println "    private Connection getConnection() throws SQLException {"
    out.println "            Connection conn;"
    out.println "            conn = ConnectionFactory.getInstance().getConnection();"
    out.println "            return conn;"
    out.println "    }"
    out.println "    "
    out.println "    public int clear() {"
    out.println ""
    out.println "    int rowsDeleted = 0;"
    out.println "    final var entities = findAll();"
    out.println "        for ( final ${className}Entity entity : entities ) {"
    out.println "            rowsDeleted += deleteById( entity.getId() );"
    out.println "        }"
    out.println "        "
    out.println "        return rowsDeleted;"
    out.println "    }"
    out.println "    "
    out.println "    "
    out.println "    public int resetSequence() {"
    out.println "    "
    out.println "        int rowsEffected = 0;"
    out.println "    "
    out.println "        try {"
    out.println "            String query = \"ALTER TABLE ${toSnakeCase(className)} AUTO_INCREMENT = 0\";"
    out.println "            connection = getConnection();"
    out.println "            statement = connection.prepareStatement( query );"
    out.println "            rowsEffected = statement.executeUpdate();"
    out.println ""
    out.println "        } catch ( SQLException sqlException ) {"
    out.println "                throw new ${className}Exception(sqlException.getMessage());"
    out.println "        } finally {"
    out.println "            try {"
    out.println "                if ( statement != null ) {"
    out.println "                    statement.close();"
    out.println "                }"
    out.println "                if ( connection != null ) {"
    out.println "                    connection.close();"
    out.println "                }"
    out.println "            } catch ( Exception exception ) {"
    out.println "                throw new ${className}Exception(exception.getMessage());"
    out.println "            }"
    out.println "        }"
    out.println ""
    out.println "        return rowsEffected;"
    out.println "    "
    out.println "    }"
    out.println "    "
    out.println "    "
    out.println "    public int save( ${className}Entity entityToSave ) throws " + className + "Exception {"
    out.println "    "
    out.println "        int rowsEffected = 0;"
    out.println "    "
    out.println "        try {"
    out.print "            String query = \"insert into ${toSnakeCase(className)}"
    out.print " ( "
    fields.each() {
        if (it != fields.first())
            if (it != fields.last())
                out.print toSnakeCase("${it.name}, ")
            else
                out.print toSnakeCase("${it.name}")
    }
    out.print " ) "
    out.print "values"
    out.print " ( "
    fields.each() {
        if (it != fields.first())
            if (it != fields.last())
                out.print "?, "
            else
                out.print "?"
    }
    out.print " )\";"
    out.println ""
    out.println "            connection = getConnection();"
    out.println "            statement = connection.prepareStatement(query);"
    fields.eachWithIndex() { it, index ->
        if (it != fields.first()) {
            out.print "            statement.set${it.type.replaceAll("java.sql.", "").replaceAll("Integer", "Int")}(${index}, entityToSave.get${it.name.capitalize()}());"
            out.println ""
        }
    }
    out.println ""
    out.println "             rowsEffected = statement.executeUpdate();"
    out.println ""
    out.println "        } catch (SQLException sqlException) {"
    out.println "                throw new ${className}Exception(sqlException.getMessage());"
    out.println "        } finally {"
    out.println "            try {"
    out.println "                if (statement != null)"
    out.println "                    statement.close();"
    out.println "                if (connection != null)"
    out.println "                    connection.close();"
    out.println "            } catch (Exception exception) {"
    out.println "                throw new ${className}Exception(exception.getMessage());"
    out.println "            }"
    out.println "        }"
    out.println ""
    out.println "        return rowsEffected;"
    out.println "    }"
    out.println ""
    out.println ""
    out.println "    public int saveAll( List<${className}>Entity entitiesToSave ) throws ${className}Exception {"
    out.println "        int noOfRowsCreated = 0;"
    out.println "        for( ${className}Entity entity : entitiesToSave){ "
    out.println "             noOfRowsCreated += save(entity); "
    out.println "        } "
    out.println "         "
    out.println "    }"
    out.println ""
    out.println ""
    out.println "    public ${className}Entity findById( Integer id ) throws " + className + "Exception {"
    out.println ""
    out.println ""
    out.println "        if( id <= 0 ) {"
    out.print "            throw new ${className}Exception( \" $className ID is required.\" )"
    out.println ".requiredFields(\"id\");"
    out.println "        }"
    out.println ""
    out.println "        ${className}Entity item = new ${className}Entity();"
    out.println ""
    out.println "        try {"
    out.print "            String query = \"select "
    fields.each() {
        if (it != fields.last())
            out.print toSnakeCase("${it.name}, ")
        else
            out.print toSnakeCase("${it.name}")
    }
    out.print " from "
    out.print toSnakeCase("$className")
    out.print " where id = ?\";"
    out.println ""
    out.println "            connection = getConnection();"
    out.println ""
    out.println "            statement = connection.prepareStatement(query);"
    out.println "            statement.setInt(1, id);"
    out.println ""
    out.println "            results = statement.executeQuery();"
    out.println "            if(results.next()){"
    fields.each() {
        out.println "                item.set${it.name.capitalize()}( results.get${it.type.replaceAll("Integer", "Int").replaceAll("java.sql.", "").capitalize()}(\"${toSnakeCase(it.name)}\") );"
    }
    out.println "            }"
    out.println "        } catch (SQLException sqlException) {"
    out.println "                throw new ${className}Exception(sqlException.getMessage());"
    out.println "        } finally {"
    out.println "            try {"
    out.println "                if (statement != null)"
    out.println "                    statement.close();"
    out.println "                if (connection != null)"
    out.println "                    connection.close();"
    out.println "            } catch (Exception exception) {"
    out.println "                throw new ${className}Exception(exception.getMessage());"
    out.println "            }"
    out.println "        }"
    out.println ""
    out.println "        return item;"
    out.println "    }"
    out.println ""
    out.println "    public List<${className}Entity> findAllByExample( ${className}Entity entityToSearch ) throws " +
            "${className}Exception {"
    out.println ""
    out.println "        final List<${className}Entity> itemList = new ArrayList<>();"
    out.println ""
    out.println "        try {"
    out.print "            String query = \"select "
    fields.each() {
        if (it != fields.last())
            out.print toSnakeCase("${it.name}, ")
        else
            out.print toSnakeCase("${it.name}")
    }
    out.print " from "
    out.print toSnakeCase("$className")
    out.print " where "
    fields.each() {
        if (it != fields.first()) {
            out.print toSnakeCase("${it.name}")
            out.print " = ?"
            if (it != fields.last())
                out.print " OR "
        }
    }
    out.print "\";"
    out.println ""
    out.println "            connection = getConnection();"
    out.println "            statement = connection.prepareStatement(query);"
    fields.eachWithIndex() { it, index ->
        if (it != fields.first()) {
            out.print "            statement.set${it.type.replaceAll("java.sql.", "").replaceAll("Integer", "Int")}( " +
                    "${index}, entityToSearch.get${it.name.capitalize()}() );"
            out.println ""
        }
    }
    out.println ""
    out.println "            results = statement.executeQuery();"
    out.println "            while(results.next()){"
    out.println "            ${className}Entity item = new ${className}Entity();"
    fields.each() {
        out.println "                item.set${it.name.capitalize()}( results.get${it.type.replaceAll("Integer", "Int").replaceAll("java.sql.", "").capitalize()}(\"${toSnakeCase(it.name)}\") );"
    }
    out.println ""
    out.println "                itemList.add(item);"
    out.println "            }"
    out.println ""
    out.println "        } catch (SQLException sqlException) {"
    out.println "                throw new ${className}Exception(sqlException.getMessage());"
    out.println "        } finally {"
    out.println "            try {"
    out.println "                if (statement != null)"
    out.println "                    statement.close();"
    out.println "                if (connection != null)"
    out.println "                    connection.close();"
    out.println "            } catch (Exception exception) {"
    out.println "                throw new ${className}Exception(exception.getMessage());"
    out.println "            }"
    out.println "        }"
    out.println ""
    out.println "        return itemList;"
    out.println "    }"
    out.println ""
    out.println "    public List<${className}Entity> findAll() throws " + className + "Exception {"
    out.println ""
    out.println "        final List<${className}Entity> itemList = new ArrayList<>();"
    out.println ""
    out.println "        try {"
    out.print "            String query = \"select "
    fields.each() {
        if (it != fields.last())
            out.print toSnakeCase("${it.name}, ")
        else
            out.print toSnakeCase("${it.name}")
    }
    out.print " from "
    out.print toSnakeCase("$className")
    out.print " \";"
    out.println ""
    out.println "            connection = getConnection();"
    out.println "            statement = connection.prepareStatement(query);"
    out.println ""
    out.println "            results = statement.executeQuery();"
    out.println "            while(results.next()){"
    out.println "                ${className}Entity item = new ${className}Entity();"
    fields.each() {
        out.println "                item.set${it.name.capitalize()}( results.get${it.type.replaceAll("Integer", "Int").replaceAll("java.sql.", "").capitalize()}(\"${toSnakeCase(it.name)}\") );"
    }
    out.println ""
    out.println "                itemList.add(item);"
    out.println "            }"
    out.println ""
    out.println "        } catch (SQLException sqlException) {"
    out.println "                throw new ${className}Exception(sqlException.getMessage());"
    out.println "        } finally {"
    out.println "            try {"
    out.println "                if (statement != null)"
    out.println "                    statement.close();"
    out.println "                if (connection != null)"
    out.println "                    connection.close();"
    out.println "            } catch (Exception exception) {"
    out.println "                throw new ${className}Exception(exception.getMessage());"
    out.println "            }"
    out.println "        }"
    out.println ""
    out.println "        return itemList;"
    out.println "    }"
    out.println ""
    out.print "    public int updateById( Integer id, ${className}Entity entityToUpdate )"
    out.println " throws ${className}Exception {"
    out.println ""
    out.println "        if( id <= 0 ) {"
    out.println "            throw new ${className}Exception( \" ${className} ID is required.\" ).requiredFields(\"id\");"
    out.println "        }"
    out.println ""
    out.println "        if ( entityToUpdate == null ) {"
    out.println "            throw new ${className}Exception( \" $className is required.\" ).null${className}Exception();"
    out.println "        }"
    out.println ""
    out.println "        int rowsEffected = 0;"
    out.println ""
    out.println "        try {"
    out.print "            String query = \""
    out.print "update "
    out.print toSnakeCase(className)
    out.print " set "
    fields.each() {
        if (it != fields.first()) {
            out.print " "
            out.print toSnakeCase(" ${it.name} ")
            out.print " = ?"
            if (it != fields.last()) {
                out.print ", "
            }
        }
    }
    out.print " where id = ?"
    out.print "\";"
    out.println ""
    out.println "            connection = getConnection();"
    out.println "            statement = connection.prepareStatement(query);"
    fields.eachWithIndex() { it, index ->
        if (it != fields.first()) {
            out.print "            statement.set${it.type.replaceAll("java.sql.", "").replaceAll("Integer", "Int")}(${index}, entityToUpdate.get${it.name.capitalize()}());"
            out.println ""
        }
    }
    out.println "            statement.setInt( ${fields.size()}, id );"
    out.println ""
    out.println "            rowsEffected = statement.executeUpdate();"
    out.println ""
    out.println "        } catch ( SQLException sqlException ) {"
    out.println "                throw new ${className}Exception(sqlException.getMessage());"
    out.println "        } finally {"
    out.println "            try {"
    out.println "                if (statement != null)"
    out.println "                    statement.close();"
    out.println "                if (connection != null)"
    out.println "                    connection.close();"
    out.println "            } catch (Exception exception) {"
    out.println "                throw new ${className}Exception(exception.getMessage());"
    out.println "            }"
    out.println "        }"
    out.println ""
    out.println "        return rowsEffected;"
    out.println "    }"
    out.println ""
    out.println "    public int deleteById( Integer id ) throws " + className + "Exception {"
    out.println ""
    out.println ""
    out.println "        if( id < 0 ) {"
    out.print "            throw new ${className}Exception( \" $className ID is required.\" )"
    out.println ".requiredFields(\"id\");"
    out.println "        }"
    out.println ""
    out.println "        int rowsEffected = 0;"
    out.println ""
    out.println "        try {"
    out.print "            String query = \"delete from "
    out.print toSnakeCase(className)
    out.print " where id = ? \";"
    out.println ""
    out.println "            connection = getConnection();"
    out.println "            statement = connection.prepareStatement(query);"
    out.println "            statement.setInt(1, id);"
    out.println ""
    out.println "            rowsEffected = statement.executeUpdate();"
    out.println "        } catch (SQLException sqlException) {"
    out.println "                throw new ${className}Exception(sqlException.getMessage());"
    out.println "        } finally {"
    out.println "            try {"
    out.println "                if (statement != null)"
    out.println "                    statement.close();"
    out.println "                if (connection != null)"
    out.println "                    connection.close();"
    out.println "            } catch (Exception exception) {"
    out.println "                throw new ${className}Exception(exception.getMessage());"
    out.println "            }"
    out.println "        }"
    out.println ""
    out.println "        return rowsEffected;"
    out.println "    }"
    fields.eachWithIndex() { it, index ->
        if (it != fields.first()) {
            out.println ""
            out.print "    public int update${it.name.capitalize()}ById( Integer id, ${it.type} ${it.name} )"
            out.println " throws ${className}Exception {"
            out.println ""
            out.println "        if( id < 0 ) {"
            out.print "            throw new ${className}Exception( \" $className ID is required.\" )"
            out.println ".requiredFields(\"id\");"
            out.println "        }"
            out.println ""
            out.println "        if ( ${it.name} == null ) {"
            out.println "            throw new ${className}Exception( \" ${it.name} is required.\" ).null${className}Exception();"
            out.println "        }"
            out.println ""
            out.println "        int rowsEffected = 0;"
            out.println ""
            out.println "        try {"
            out.print "            String query = \""
            out.print "update "
            out.print toSnakeCase(className)
            out.print " set "
            out.print toSnakeCase(" $it.name ")
            out.print " = ?"
            out.print " where id = ?"
            out.print "\";"
            out.println ""
            out.println ""
            out.println "            connection = getConnection();"
            out.println "            statement = connection.prepareStatement(query);"
            out.println "            statement.set${it.type.replaceAll("java.sql.", "").replaceAll("Integer", "Int")}( 1, ${it.name} );"
            out.println "            statement.setInt( 2, id );"
            out.println ""
            out.println "            rowsEffected = statement.executeUpdate();"
            out.println ""
            out.println "        } catch (SQLException sqlException) {"
            out.println "                throw new ${className}Exception(sqlException.getMessage());"
            out.println "        } finally {"
            out.println "            try {"
            out.println "                if (statement != null)"
            out.println "                    statement.close();"
            out.println "                if (connection != null)"
            out.println "                    connection.close();"
            out.println "            } catch (Exception exception) {"
            out.println "                throw new ${className}Exception(exception.getMessage());"
            out.println "            }"
            out.println "        }"
            out.println ""
            out.println "        return rowsEffected;"
            out.println "    }"
            out.println ""
        }
    }
    fields.eachWithIndex() { it, index ->
        if (it != fields.first()) {
            out.println ""
            out.print "    public List<${className}Entity> searchBy${it.name.capitalize()}( ${it.type} ${it.name} )"
            out.println " throws ${className}Exception {"
            out.println ""
            out.println "        final List<${className}Entity> itemList = new ArrayList<>();"
            out.println ""
            out.println "        try {"
            out.print "            String query = \"select "
            fields.each() {
                if (it != fields.last())
                    out.print toSnakeCase("${it.name}, ")
                else
                    out.print toSnakeCase("${it.name}")
            }
            out.print " from "
            out.print toSnakeCase("$className")
            out.print " where "
            out.print toSnakeCase("${it.name}")
            if (it.type == 'String')
                out.print " LIKE ?"
            else
                out.print " = ?"
            out.print "\";"
            out.println ""
            out.println "            connection = getConnection();"
            out.println "            statement = connection.prepareStatement(query);"
            out.println "            statement.set${it.type.replaceAll("java.sql.", "").replaceAll("Integer", "Int")}( 1, ${it.name} );"
            out.println ""
            out.println "            results = statement.executeQuery();"
            out.println ""
            out.println "            while( results.next() ) {"
            out.println "            ${className}Entity item = new ${className}Entity();"
            fields.each() {
                out.println "                item.set${it.name.capitalize()}( results.get${it.type.replaceAll("Integer", "Int").replaceAll("java.sql.", "").capitalize()}(\"${toSnakeCase(it.name)}\") );"
            }
            out.println ""
            out.println "                itemList.add(item);"
            out.println "            }"
            out.println ""
            out.println "        } catch (SQLException sqlException) {"
            out.println "                throw new ${className}Exception(sqlException.getMessage());"
            out.println "        } finally {"
            out.println "            try {"
            out.println "                if (statement != null)"
            out.println "                    statement.close();"
            out.println "                if (connection != null)"
            out.println "                    connection.close();"
            out.println "            } catch (Exception exception) {"
            out.println "                throw new ${className}Exception(exception.getMessage());"
            out.println "            }"
            out.println "        }"
            out.println ""
            out.println "        return itemList;"
            out.println "    }"
        }
        out.println ""
        out.println ""
        out.println "    public boolean existsBy${it.name.capitalize()}( ${it.type} ${it.name} ) throws ${className}Exception {"
        out.println ""
        out.println "        boolean exists = false;"
        out.println "        try {"
        out.print "            String query = \"select "
        fields.each() {
            if (it != fields.last())
                out.print toSnakeCase("${it.name}, ")
            else
                out.print toSnakeCase("${it.name}")
        }
        out.print " from "
        out.print toSnakeCase("$className")
        out.print " where "
        out.print toSnakeCase("${it.name}")
        if (it.type == 'String')
            out.print " LIKE ?"
        else
            out.print " = ?"
        out.print "\";"
        out.println ""
        out.println "            connection = getConnection();"
        out.println "            statement = connection.prepareStatement(query);"
        out.println "            statement.set${it.type.replaceAll("java.sql.", "").replaceAll("Integer", "Int")}( 1, ${it.name} );"
        out.println ""
        out.println "            results = statement.executeQuery();"
        out.println "            exists = results.next();"
        out.println ""
        out.println "        } catch (SQLException sqlException) {"
        out.println "                throw new ${className}Exception(sqlException.getMessage());"
        out.println "        } finally {"
        out.println "            try {"
        out.println "                if (statement != null)"
        out.println "                    statement.close();"
        out.println "                if (connection != null)"
        out.println "                    connection.close();"
        out.println "            } catch (Exception exception) {"
        out.println "                throw new ${className}Exception(exception.getMessage());"
        out.println "            }"
        out.println "        }"
        out.println ""
        out.println "        return exists;"
        out.println "    }"
    }
    out.println ""
    out.println ""
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        fields += [[
                           name : javaName(col.getName(), false),
                           type : typeStr,
                           annos: ""]]
    }
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

def toCamelCase(String text, boolean capitalized = false) {
    text = text.replaceAll("(_)([A-Za-z0-9])", { Object[] it -> it[2].toUpperCase() })
    return capitalized ? capitalize(text) : text
}

def toSnakeCase(String text) {
    text.replaceAll(/([A-Z])/, /_$1/).toLowerCase().replaceAll(/^_/, '')
}