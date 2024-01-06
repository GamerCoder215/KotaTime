package me.gamercoder215.kotatime.storage

// API Data

enum class WCategory(val color: Int) {
    BROWSING(0xd2891a),
    BUILDING(0xe8ce3d),
    CODE_REVIEWING(0x34f5db),
    CODING(0x1f9aef),
    COMMUNICATING(0x6f2170),
    DEBUGGING(0xc49af9),
    DESIGNING(0x9061ca),
    INDEXING(0xfff09d),
    LEARNING(0x38a2eb),
    MANUAL_TESTING(0x34be61),
    MEETING(0xfd1464),
    PLANNING(0xff4585),
    RESEARCHING(0x4cbfc0),
    RUNNING_TESTS(0xec5756),
    TRANSLATING(0x5a42cf),
    WRITING_DOCS(0x36e3ff),
    WRITING_TESTS(0x3cec76)
}

enum class WEditor(
    val color: Int,
    vararg otherNames: String
) {

    // Editors
    ANDROID_STUDIO(0x99cd00),
    APP_CODE(0x04dbde),
    APTANA(0xec8623),
    AQUA(0x3ae868),
    ARDURINO_IDE(0x048184),
    ATOM(0x49b77e),
    AZURE_DATA_STUDIO(0x0271c6),
    BRACKETS(0x067dc3),
    CPP_BUILDER(0x8a1922, "C++ Builder"),
    CLION(0x14c9a5),
    CLOUD9(0x25a6d9),
    CODETASTY(0x7368a8),
    DATAGRIP(0x907cf2),
    DATASPELL(0x087cfa),
    DBEAVER(0x897363),
    DELPHI(0xdb2129),
    ECLIPSE(0x443582),
    EMACS(0x8c76c3),
    ERIC(0x423f13),
    EXCEL(0x0f753c),
    FLASH_BUILDER(0xaca3a4),
    GEDIT(0x872114),
    GOLAND(0x872114),
    HBUILDER_X(0x1ba334),
    IDA_PRO(0x35c4c0),
    INTELLIJ_IDEA(0x2876e1, "Intellijidea"),
    KAKOUNE(0xdd5f4a),
    KATE(0x0b7aef),
    KOMODO(0xfcb414),
    MICRO(0x2c3494),
    NEOVIM(0x068304),
    NETBEANS(0xaed43a),
    NOTEPAD_PLUS_PLUS(0x9ecf54, "Notepad++"),
    NOVA(0xff054a),
    OBSIDIAN(0x4a38a0),
    ONIVIM(0xee848e),
    PHPSTORM(0xd93ac1),
    POSTMAN(0xfc6b33),
    PULSAR(0x662d91),
    PYCHARM(0xd2ee5c),
    PYMAKR(0x323d4f),
    RECLASSEX(0xb6c2e4),
    RIDER(0xf7a415),
    RUBYMINE(0xff6336),
    RUSTROVER(0xff2f52),
    SLICKEDIT(0x57ca57),
    SQL_SERVER_MANAGEMENT_STUDIO(0xffb901, "Microsoft SQL Server Management Studio"),
    SUBLIME_TEXT(0xff9800, "Sublime"),
    TERMINAL(0x133f1c, "Terminal.app"),
    TEX_STUDIO(0x652d96),
    TEXTMATE(0x822b7a),
    UNITY(0x222d36),
    VIM(0x068304),
    VISUAL_STUDIO(0x9460cd, "VS", "Visual Studio for Mac", "Xamarin"),
    VS_CODE(0x027acd, "VSCode", "Visual Studio Code"),
    WEBSTORM(0x00c6d7),
    WING(0xb3b3b3),
    WORD(0x0f4091, "Microsoft Word"),
    XCODE(0x3fa7e4),

    // Browsers
    CHROME(0xfdd308),
    EDGE(0x3acca9),
    FIREFOX(0xd96527),

    // Other
    ADOBE_XD(0xfd27bc),
    BLENDER(0xfb8007),
    CANVA(0x288bda),
    CODA(0x3e8e1c),
    DISCORD(0x5c64f4),
    FIGMA(0xc7b9ff),
    MPS(0x12a8d5),
    POWERPOINT(0xc6421f),
    PROCESSING(0x6a7152),
    SIYUAN(0xff2f52),
    SKETCH(0xfdad00),

    ;

    val otherNames: Set<String> = setOf(*otherNames)

    companion object {

        fun fromName(name: String): WEditor? {
            val name0 = name.replace(' ', '_').lowercase()

            for (editor in entries) {
                val names = (editor.otherNames + editor.name).map { it.replace(' ', '_').lowercase() }
                if (names.contains(name0)) return editor
            }

            return null
        }

    }

}