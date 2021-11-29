import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = "be.intec.services.flow"
entityPackageName = packageName.replaceAll(".services.flow", ".models.entities")
repositoryPackageName = packageName.replaceAll(".services.flow", ".repositories")
dataBinderPackageName = packageName.replaceAll(".services.flow", ".models.binders")
exceptionsPackageName = packageName.replaceAll(".services.flow", ".services.exceptions")

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

    new File(dir, className + "Service.java").withPrintWriter { out -> generateSourceCode(out, className, fields) }
}

def generateSourceCode(out, className, fields) {
    out.println "package $packageName;"
    out.println ""
    out.println "import $entityPackageName.*;"
    out.println "import java.sql.*;"
    out.println "import java.util.*;"
    out.println "import $exceptionsPackageName.*;"
    out.println "import $repositoryPackageName.*;"
    out.println ""
    out.println "public class ${className}Service {"
    out.println ""
    out.println "    private final ${className}Repository repository;"
    out.println "    "
    out.println "    public ${className}Service() { "
    out.println "        this.repository = new ${className}Repository();"
    out.println "    }"
    out.println "    "
    out.println "    public int clear() {"
    out.println ""
    out.println "        final var rowsDeleted = repository.clear();"
    out.println "    "
    out.println "        return rowsDeleted;"
    out.println "    }"
    out.println "    "
    out.println "    "
    out.println "    public int resetSequence() {"
    out.println "    "
    out.println "        final var rowsEffected = repository.resetSequence();"
    out.println ""
    out.println "        return rowsEffected;"
    out.println "    "
    out.println "    }"
    out.println "    "
    out.println "    "
    out.println "    public int save( ${className}Entity record ) throws " + className + "Exception {"
    out.println "    "
    out.println "        int rowsEffected = repository.save( record ); "
    out.println ""
    out.println "        return rowsEffected;"
    out.println "    }"
    out.println ""
    out.println "    public ${className}Entity findById( Integer id ) throws " + className + "Exception {"
    out.println ""
    out.println "        final var found${className} = repository.findById( id );"
    out.println ""
    out.println "        return found${className};"
    out.println "    }"
    out.println ""
    out.print "    public List<${className}Entity> findAllByExample( ${className}Entity example )"
    out.println " throws ${className}Exception {"
    out.println ""
    out.println "        final List<${className}Entity> itemList = repository.findAllByExample(example);"
    out.println ""
    out.println "        return itemList;"
    out.println "    }"
    out.println ""
    out.println "    public List<${className}Entity> findAll() throws " + className + "Exception {"
    out.println ""
    out.println "        final List<${className}Entity> itemList = repository.findAll();"
    out.println ""
    out.println "        return itemList;"
    out.println "    }"
    out.println ""
    out.println "    public int updateById( Integer id, ${className}Entity record ) throws ${className}Exception {"
    out.println ""
    out.println "        int rowsEffected = repository.updateById(id, record);"
    out.println ""
    out.println "        return rowsEffected;"
    out.println "    }"
    out.println ""
    out.println "    public int deleteById( Integer id ) throws " + className + "Exception {"
    out.println ""
    out.println "        int rowsDeleted = repository.deleteById( id );"
    out.println ""
    out.println "        return rowsDeleted;"
    out.println "    }"
    fields.eachWithIndex() { it, index ->
        if (it != fields.first()) {
            out.println ""
            out.print "    public int update${it.name.capitalize()}ById( Integer id, ${it.type} ${it.name} )"
            out.println " throws ${className}Exception {"
            out.println ""
            out.println "        int rowsEffected = repository.update${it.name.capitalize()}ById( id, ${it.name} );"
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
            out.print "   final List<${className}Entity> itemList ="
            out.println"        repository.searchBy${it.name.capitalize()}( ${it.name} );"
            out.println ""
            out.println "        return itemList;"
            out.println "    }"
        }
        out.println ""
        out.println ""
        out.println "    public boolean existsBy${it.name.capitalize()}( ${it.type} ${it.name} ) throws ${className}Exception {"
        out.println ""
        out.println "        boolean exists = repository.existsBy${it.name.capitalize()}( ${it.name} );"
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