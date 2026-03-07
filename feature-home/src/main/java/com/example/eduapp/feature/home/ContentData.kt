package com.example.eduapp.feature.home


import com.example.eduapp.core.domain.model.Component

object ContentData {

    val componentsByCategory: Map<String, List<Component>> = mapOf(
        "cat_negizgi" to listOf(
            Component(
                id = "comp_juyelik_blok",
                categoryId = "cat_negizgi",
                title = "Жүйелік блок",
                description = "Жүйелік блок – компьютердің негізгі бөлігі, барлық ішкі құрылғылар орналастырылған корпус. Онда процессор, жад, аналық плата, қуат көзі және басқа компоненттер орналасады.",
                composition = listOf(
                    "Процессор (CPU)",
                    "Аналық плата",
                    "Жедел жады (RAM)",
                    "Қуат көзі (PSU)",
                    "Сақтау құрылғылары (HDD/SSD)"
                ),
                function = "Барлық есептеу процестерін орындап, құрылғылардың жұмысын басқарады.",
                modelFileName = "models/PC.glb"
            ),
            Component(
                id = "comp_processor",
                categoryId = "cat_negizgi",
                title = "Процессор (CPU)",
                description = "Процессор (Central Processing Unit) – компьютердің «миы». Ол барлық есептеу операцияларын орындайды, бағдарламалардың нұсқауларын өңдейді және жүйенің жалпы жұмысын басқарады.",
                composition = listOf(
                    "Арифметикалық-логикалық құрылғы (ALU)",
                    "Басқару құрылғысы (CU)",
                    "Кэш жады (L1, L2, L3)",
                    "Регистрлер"
                ),
                function = "Бағдарламалардың командаларын орындау, деректерді өңдеу, арифметикалық және логикалық операцияларды жүзеге асыру.",
                modelFileName = "models/CPU.glb"
            ),
            Component(
                id = "comp_jady",
                categoryId = "cat_negizgi",
                title = "Компьютер жады\n(ішкі/Сыртқы)",
                description = "Компьютер жады деректерді сақтау үшін қолданылады. Ішкі жады (RAM, ROM) – уақытша және тұрақты деректерді сақтайды. Сыртқы жады (HDD, SSD, USB) – ұзақ мерзімді сақтау.",
                composition = listOf(
                    "Жедел жады (RAM) – уақытша сақтау",
                    "Тұрақты жады (ROM) – жүйелік деректер",
                    "Қатты диск (HDD) – магниттік сақтау",
                    "SSD – жартылай өткізгішті диск",
                    "USB флеш-жады"
                ),
                function = "Деректер мен бағдарламаларды уақытша немесе тұрақты сақтау, процессорға қажетті ақпаратты жылдам беру.",
                modelFileName = "models/storage_ssd_hdd_m.2.glb"
            ),
            Component(
                id = "comp_analyk_plata",
                categoryId = "cat_negizgi",
                title = "Аналық плата\n(Motherboard)",
                description = "Аналық плата – компьютердің негізгі платасы, барлық компоненттерді бір-бірімен байланыстырады. Процессор, жад, бейне карта және басқа құрылғылар осы платаға қосылады.",
                composition = listOf(
                    "Процессор ұясы (Socket)",
                    "RAM слоттары",
                    "Чипсет (Солтүстік және Оңтүстік көпір)",
                    "PCI-E слоттары",
                    "SATA порттары",
                    "BIOS/UEFI чипі"
                ),
                function = "Барлық компоненттер арасындағы деректер алмасуын қамтамасыз ету, құрылғыларды біріктіру.",
                modelFileName = "models/motherboard.glb"
            ),
            Component(
                id = "comp_kuat_kozi",
                categoryId = "cat_negizgi",
                title = "Қуат көзі\n(Power Supply)",
                description = "Қуат көзі (PSU) – айнымалы токты тұрақты токқа түрлендіріп, компьютердің барлық компоненттерін электр қуатымен қамтамасыз етеді.",
                composition = listOf(
                    "Трансформатор",
                    "Түзеткіш",
                    "Сүзгіш",
                    "Қорғаныс тізбектері",
                    "Салқындату желдеткіші"
                ),
                function = "220V айнымалы токты 3.3V, 5V, 12V тұрақты токқа түрлендіріп, компоненттерді қуатпен қамтамасыз ету.",
                modelFileName = "models/power_supply.glb"
            ),
            Component(
                id = "comp_salkyndatu",
                categoryId = "cat_negizgi",
                title = "Салқындату жүйесі",
                description = "Салқындату жүйесі компьютер компоненттерінің қызып кетуін болдырмайды. Процессор, бейне карта және басқа құрылғылардың температурасын қалыпты деңгейде ұстайды.",
                composition = listOf(
                    "Ауа салқындатқышы (кулер + радиатор)",
                    "Сұйық салқындату жүйесі",
                    "Термопаста",
                    "Корпус желдеткіштері"
                ),
                function = "Құрылғылардың температурасын қалыпты деңгейде ұстау, қызып кетуден қорғау, жүйенің тұрақты жұмысын қамтамасыз ету.",
                modelFileName = "models/cooler.glb"
            )
        ),
        "cat_engizu" to listOf(
            Component(
                id = "comp_keyboard",
                categoryId = "cat_engizu",
                title = "Пернетақта (Keyboard)",
                description = "Пернетақта – мәтін енгізу және компьютерді басқару үшін қолданылатын негізгі енгізу құрылғысы.",
                composition = listOf("Механикалық пернетақта", "Мембраналық пернетақта", "Сымсыз пернетақта"),
                function = "Мәтін, сандар және командаларды компьютерге енгізу.",
                modelFileName = "models/keyboard.glb"
            ),
            Component(
                id = "comp_mouse",
                categoryId = "cat_engizu",
                title = "Тышқан (Mouse)",
                description = "Тышқан – экрандағы курсорды басқару үшін қолданылатын координаталық енгізу құрылғысы.",
                composition = listOf("Оптикалық тышқан", "Лазерлік тышқан", "Сымсыз тышқан"),
                function = "Курсорды жылжыту, объектілерді таңдау, мәзірлерді ашу.",
                modelFileName = "models/mouse.glb"
            ),
            Component(
                id = "comp_microphone",
                categoryId = "cat_engizu",
                title = "Микрофон",
                description = "Микрофон – дыбыс толқындарын электр сигналдарына түрлендіретін енгізу құрылғысы.",
                composition = listOf("Динамикалық микрофон", "Конденсаторлық микрофон", "USB микрофон"),
                function = "Дауысты жазу, бейне қоңыраулар, дауыстық командалар.",
                modelFileName = "models/microphone.glb"
            ),
            Component(
                id = "comp_webcam",
                categoryId = "cat_engizu",
                title = "Веб-камера",
                description = "Веб-камера – бейне сигналды компьютерге беретін енгізу құрылғысы.",
                composition = listOf("Кірістірілген камера", "Сыртқы USB камера"),
                function = "Бейне қоңыраулар, бейне жазу, онлайн хабар тарату.",
                modelFileName = "models/webcam.glb"
            )
        ),
        "cat_shygaru" to listOf(
            Component(
                id = "comp_monitor",
                categoryId = "cat_shygaru",
                title = "Монитор",
                description = "Монитор – компьютерден шығатын ақпаратты визуалды түрде көрсететін шығару құрылғысы.",
                composition = listOf("LCD монитор", "LED монитор", "OLED монитор", "IPS панель"),
                function = "Мәтін, сурет, бейне және графикалық ақпаратты экранда көрсету.",
                modelFileName = "models/monitor.glb"
            ),
            Component(
                id = "comp_printer",
                categoryId = "cat_shygaru",
                title = "Принтер",
                description = "Принтер – цифрлық ақпаратты қағазға басып шығаратын құрылғы.",
                composition = listOf("Лазерлік принтер", "Сиялы принтер", "Матрицалық принтер", "3D принтер"),
                function = "Мәтін, кесте, сурет және басқа ақпаратты қағазға басып шығару.",
                modelFileName = "models/printer.glb"
            ),
            Component(
                id = "comp_speaker",
                categoryId = "cat_shygaru",
                title = "Дыбыс колонкалары",
                description = "Дыбыс колонкалары – электр сигналдарын дыбыс толқындарына түрлендіретін шығару құрылғысы.",
                composition = listOf("Сымды колонкалар", "Bluetooth колонкалар", "Құлаққаптар"),
                function = "Музыка, бейне дыбыстары, жүйелік дыбыстарды шығару.",
                modelFileName = "models/speakers.glb"
            ),
            Component(
                id = "comp_projector",
                categoryId = "cat_shygaru",
                title = "Проектор",
                description = "Проектор – компьютердегі кескінді үлкейтіп экранға немесе қабырғаға проекциялайтын құрылғы.",
                composition = listOf("DLP проектор", "LCD проектор", "LED проектор"),
                function = "Презентациялар, фильмдер және кескіндерді үлкен экранға көрсету.",
                modelFileName = "models/GPU.glb"
            )
        ),
        "cat_princip" to listOf(
            Component(
                id = "comp_input_process",
                categoryId = "cat_princip",
                title = "Енгізу (Input)",
                description = "Пайдаланушы пернетақта, тышқан, микрофон немесе басқа енгізу құрылғылары арқылы деректерді компьютерге жібереді.",
                composition = listOf("Пернетақта арқылы мәтін енгізу", "Тышқан арқылы команда беру", "Сканер арқылы құжат енгізу"),
                function = "Сыртқы ортадан ақпаратты компьютерге беру.",
                modelFileName = "models/keyboard.glb"
            ),
            Component(
                id = "comp_processing",
                categoryId = "cat_princip",
                title = "Өңдеу (Processing)",
                description = "Процессор (CPU) енгізілген деректерді бағдарлама нұсқауларына сәйкес өңдейді. Арифметикалық және логикалық операциялар орындалады.",
                composition = listOf("Командаларды оқу (Fetch)", "Командаларды талдау (Decode)", "Командаларды орындау (Execute)", "Нәтижені жазу (Store)"),
                function = "Деректерді бағдарлама бойынша өңдеп, нәтиже шығару.",
                modelFileName = "models/CPU.glb"
            ),
            Component(
                id = "comp_output_process",
                categoryId = "cat_princip",
                title = "Шығару (Output)",
                description = "Өңделген нәтижелер монитор, принтер, колонкалар арқылы пайдаланушыға көрсетіледі.",
                composition = listOf("Монитордағы кескін", "Принтердегі басылым", "Колонкадағы дыбыс"),
                function = "Өңделген ақпаратты пайдаланушыға жеткізу.",
                modelFileName = "models/monitor.glb"
            ),
            Component(
                id = "comp_storage_process",
                categoryId = "cat_princip",
                title = "Сақтау (Storage)",
                description = "Деректер жедел жадта (RAM) уақытша немесе қатты дискіде (HDD/SSD) тұрақты сақталады.",
                composition = listOf("RAM – жұмыс кезінде уақытша сақтау", "HDD/SSD – ұзақ мерзімді сақтау", "Кэш – жылдам қол жеткізу"),
                function = "Деректер мен бағдарламаларды кейін қолдану үшін сақтау.",
                modelFileName = "models/hdd.glb"
            )
        ),
        "cat_architecture" to listOf(
            Component(
                id = "comp_cloud",
                categoryId = "cat_architecture",
                title = "Бұлттық технологиялар (Cloud)",
                description = "Бұлттық есептеулер – интернет арқылы серверлерде деректерді сақтау, өңдеу және қолдану технологиясы.",
                composition = listOf("IaaS – инфрақұрылым қызметі", "PaaS – платформа қызметі", "SaaS – бағдарлама қызметі"),
                function = "Деректерді қашықтан сақтау, бағдарламаларды серверде орындау, ресурстарды масштабтау."
            ),
            Component(
                id = "comp_ai",
                categoryId = "cat_architecture",
                title = "Жасанды интеллект (AI)",
                description = "Жасанды интеллект – машиналардың адам секілді ойлау, үйрену және шешім қабылдау қабілетін модельдейтін технология.",
                composition = listOf("Машиналық оқыту (ML)", "Терең оқыту (Deep Learning)", "Нейрондық желілер", "Табиғи тілді өңдеу (NLP)"),
                function = "Деректерді талдау, болжау, тілді түсіну, кескіндерді тану."
            ),
            Component(
                id = "comp_iot",
                categoryId = "cat_architecture",
                title = "Заттар интернеті (IoT)",
                description = "IoT – физикалық құрылғылардың интернет арқылы деректер алмасуы және өзара байланысуы.",
                composition = listOf("Сенсорлар", "Ақылды үй құрылғылары", "Өнеркәсіптік IoT", "Денсаулық сақтау құрылғылары"),
                function = "Құрылғылар арасында деректер алмасу, автоматтандыру, қашықтан басқару."
            ),
            Component(
                id = "comp_blockchain",
                categoryId = "cat_architecture",
                title = "Блокчейн технологиясы",
                description = "Блокчейн – деректерді тізбекті блоктарда орталықсыз сақтайтын технология. Деректерді өзгерту мүмкін емес.",
                composition = listOf("Блоктар тізбегі", "Криптографиялық хэштеу", "Смарт-контракттар", "Консенсус алгоритмдері"),
                function = "Қаржылық транзакциялар, деректердің тұтастығын қамтамасыз ету, орталықсыз қосымшалар."
            )
        )
    )

    fun getComponentsForCategory(categoryId: String): List<Component> {
        return componentsByCategory[categoryId] ?: emptyList()
    }

    fun getComponentById(componentId: String): Component? {
        return componentsByCategory.values.flatten().find { it.id == componentId }
    }
}
