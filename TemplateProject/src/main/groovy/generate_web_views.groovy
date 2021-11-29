import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = "be.intec.views.web"
entityPackageName = packageName.replaceAll(".views.desktop", ".models.entities")
dataBinderPackageName = packageName.replaceAll(".views.desktop", ".models.binders")
repositoriesPackageName = packageName.replaceAll(".views.desktop", ".repositories")
exceptionsPackageName = packageName.replaceAll(".views.desktop", ".services.exceptions")

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
    def menuSourceCode = new File(dir, "MainWebView.java");
    if (!menuSourceCode.exists()) {
        menuSourceCode.withPrintWriter { out -> generateMenuSourceCode(out) }
    }

    def templateSourceCode = new File(dir, "MainWebView.java");
    if (!templateSourceCode.exists()) {
        templateSourceCode.withPrintWriter { out -> generateTemplateSourceCode(out) }
    }

    new File(dir, className + "WebView.java").withPrintWriter { out -> generateSourceCode(out, className, fields) }
}

def generateMenuSourceCode(out) {

    out.println "package ${packageName};\n" +
            "\n" +
            "\n" +
            "import ${servicesPackageName}.*;\n" +
            "import com.vaadin.flow.component.dependency.CssImport;\n" +
            "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" +
            "import com.vaadin.flow.router.Route;\n" +
            "import com.vaadin.flow.server.PWA;\n" +
            "\n" +
            "@Route(\"\")\n" +
            "@PWA(name = \"App Name with Description\", shortName = \"AppShortName\" )\n" +
            "@CssImport(\"./styles/shared-styles.css\")\n" +
            "public class MainWebView extends VerticalLayout {\n" +
            "\n" +
            "    private final Service1 service;\n" +
            "\n" +
            "    public MainWebView() {\n" +
            "\n" +
            "        this.service = new Service1();\n" +
            "\n" +
            "        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.\n" +
            "        addClassName(\"centered-content\");\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public Service1 getService() {\n" +
            "\n" +
            "        return service;\n" +
            "    }\n" +
            "\n" +
            "}\n";
}

def generateTemplateSourceCode(out) {

    out.println "package ${packageName};\n" +
            "\n" +
            "\n" +
            "import com.vaadin.flow.router.PageTitle;\n" +
            "import com.vaadin.flow.router.Route;\n" +
            "\n" +
            "@PageTitle( \"Add Page Title Here\" )\n" +
            "@Route(\"set-page-route-here\")\n" +
            "public class TemplateWebView extends MainWebView {\n" +
            "\n" +
            "    public TemplateWebView() {\n" +
            "\n" +
            "\n" +
            "\n" +
            "        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.\n" +
            "        addClassName(\"centered-content\");\n" +
            "\n" +
            "    }\n" +
            "}\n";
}

def generateSourceCode(out, className, fields) {
    out.println ""
    out.println "package ${packageName};"
    out.println ""
    out.println "import ${exceptionsPackageName}.*;"
    out.println "import ${entityPackageName}.*;"
    out.println "import ${dataBinderPackageName}.*;"
    out.println "import ${repositoriesPackageName}.*;"
    out.println ""
    out.println "import java.util.List;"
    out.println ""
    out.println "import com.vaadin.flow.router.PageTitle;"
    out.println "import com.vaadin.flow.router.Route;"
    out.println ""
    out.println "@PageTitle( \"Add Page Title Here\" )"
    out.println ""
    out.println "public class ${className}WebView extends Application {"
    out.println ""
    out.println "    private final TableView<${className}Binder> table = new TableView<>();"
    out.println "    private final ObservableList<${className}Binder> data = FXCollections.observableArrayList();"
    out.println "    private final ${className}Repository repository = new ${className}Repository();"
    out.println "    final HBox actBox = new HBox();"
    out.println "    final HBox navBox = new HBox();"
    out.println ""
    out.println "    public static void main(String[] args) {"
    out.println "        launch(args);"
    out.println "    }"
    out.println ""
    out.println "    @Override"
    out.println "    public void start(Stage stage) {"
    out.println ""
    out.println "        Scene scene = new Scene(new Group());"
    out.println "        stage.setTitle(\"${className} Desktop View\");"
    out.println "        stage.setWidth(500);"
    out.println "        stage.setHeight(650);"
    out.println ""
    out.println "        final Label header = new Label(\"${className} Records\");"
    out.println "        header.setFont(new Font(\"Arial\", 14));"
    out.println ""
    out.println "        final Label message = new Label(\"Status: Form loaded.\");"
    out.println "        header.setFont(new Font(\"Arial\", 10));"
    out.println ""
    out.println "        table.setEditable(true);"
    out.println ""
    fields.eachWithIndex() { it, index ->
        out.println "        TableColumn column${index} = new TableColumn(\"${it.name}\");"
        out.println "        column${index}.setCellValueFactory("
        out.println "                new PropertyValueFactory<${className}Binder, ${it.type}>(\"${it.name}\"));"
        if (it == fields.first()) {
            out.println "        column${index}.setEditable(false);"
        } else {
            out.println "        column${index}.setEditable(true);"
            out.println "        column${index}.setOnEditCommit((EventHandler<CellEditEvent<${className}Binder, ${it.type}>>) t -> {"
            out.println ""
            out.println "                    // UPDATE COLUMN IN THE REPO"
            out.println ""
            out.println ""
            out.println "                    // UPDATE TABLE COLUMN"
            out.println ""
            out.println "                    t.getTableView().getItems().get("
            out.println "                            t.getTablePosition().getRow()).set${it.name.capitalize()}(t.getNewValue());"

            out.println "                }"
            out.println "        );"
        }
    }
    out.println ""
    out.println "        table.setItems(data);"
    out.print "        table.getColumns().addAll("
    fields.eachWithIndex() { it, index ->
        out.print "column${index}"
        if (it != fields.last())
            out.print ", "
    }
    out.print ");"
    out.println ""
    out.println ""
    fields.eachWithIndex() { it, index ->
        if (it != fields.first()) {
            out.println "        final TextField column${index}EditField = new TextField();"
            out.println "        column${index}EditField.setPromptText(\"${it.name}\");"
            out.println "        column${index}EditField.setMaxWidth(column${index}.getPrefWidth());"
        }
    }
    out.println ""
    out.println "        final Button addButton = new Button(\"Add\");"
    out.println "        addButton.setOnAction(onClick -> {"
    out.println ""
    out.println "            // SAVE NEW RECORD TO DB"
    out.println "            try {"
    out.println "                int noOfNewRecords = repository.save("
    out.println "                        new ${className}Entity()"
    fields.eachWithIndex() { it, index ->
        if (it != fields.first())
            out.println "                                .withParsed${it.name.capitalize()}(column${index}EditField.getText())"
    }
    out.println "                );"
    out.println ""
    out.println "                if (noOfNewRecords > 0) {"
    out.println ""
    out.println "            // CLEAR TABLE"
    out.println "            table.getItems().clear();"
    out.println ""
    out.println "            // MAP DB RECORDS TO VIEW COMPONENTS"
    out.println "            List<${className}Entity> itemList = repository.findAll();"
    out.println ""
    out.println "            for (${className}Entity item : itemList) {"
    out.println "                data.add(new ${className}Binder("
    fields.eachWithIndex() { it, index ->
        out.print "                        item.get${it.name.capitalize()}()"
        if (it != fields.last())
            out.print ", "
        out.println ""
    }
    out.println "                ));"
    out.println "            }"
    out.println ""
    out.println "            table.setItems(data);"
    out.println ""
    out.println "            message.setText(\"New records from ${className} are created and to the list.\");"
    fields.eachWithIndex() { it, index ->
        if (it != fields.first())
            out.println "                    column${index}EditField.clear();"
    }
    out.println ""
    out.println "                    message.setStyle(\"-fx-text-fill: green; -fx-font-size: 16px;\");"
    out.println "                    message.setText(\"${className} created!\");"
    out.println ""
    out.println "                } else {"
    out.println "                    message.setStyle(\"-fx-text-fill: red; -fx-font-size: 16px;\");"
    out.println "                    message.setText(\"ERROR: ${className} could NOT be created!\");"
    out.println "                }"
    out.println "            } catch (${className}Exception ${className.toLowerCase()}Exception) {"
    out.println "                message.setStyle(\"-fx-text-fill: red; -fx-font-size: 16px;\");"
    out.println "                message.setText(${className.toLowerCase()}Exception.getMessage());"
    out.println "            }"
    out.println ""
    out.println "        });"
    out.println ""
    out.println "        final Button viewButton = new Button(\"View\");"
    out.println "        viewButton.setOnAction(onClick -> {"
    out.println ""
    out.println "            // CLEAR TABLE"
    out.println "            table.getItems().clear();"
    out.println ""
    out.println "            // MAP DB RECORDS TO VIEW COMPONENTS"
    out.println "            List<${className}Entity> itemList = repository.findAll();"
    out.println ""
    out.println "            for (${className}Entity item : itemList) {"
    out.println "                data.add(new ${className}Binder("
    fields.eachWithIndex() { it, index ->
        out.print "                        item.get${it.name.capitalize()}()"
        if (it != fields.last())
            out.print ", "
        out.println ""
    }
    out.println "                ));"
    out.println "            }"
    out.println ""
    out.println "            table.setItems(data);"
    out.println ""
    out.println "            message.setText(\"${className} records are refreshed\");"
    out.println ""
    out.println "        });"
    out.println ""
    out.println "        final Button backButton = new Button(\"Back\");"
    out.println "        backButton.setOnAction(onClick -> {"
    out.println "            scene.getWindow().hide();"
    out.println "            new MainMenuDesktopView().start(stage);"
    out.println "        });"
    out.println ""
    out.println "        navBox.getChildren().addAll(backButton);"
    out.println "        navBox.setSpacing(3);"
    out.println ""
    out.print "        actBox.getChildren().addAll("
    fields.eachWithIndex() { it, index ->
        if (it != fields.first()) {
            out.print "column${index}EditField"
            if (it != fields.last())
                out.print ","
        }
    }
    out.print ", addButton, viewButton);"
    out.println ""
    out.println "        actBox.setSpacing(3);"
    out.println ""
    out.println "        final VBox vbox = new VBox();"
    out.println "        vbox.setSpacing(5);"
    out.println "        vbox.setPadding(new Insets(10, 0, 0, 10));"
    out.println "        vbox.getChildren().addAll(header, table, actBox, navBox, message);"
    out.println ""
    out.println "        ((Group) scene.getRoot()).getChildren().addAll(vbox);"
    out.println ""
    out.println "        stage.setScene(scene);"
    out.println ""
    out.println "        stage.show();"
    out.println "    }"
    out.println ""
    out.println "}"
    out.println ""
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
