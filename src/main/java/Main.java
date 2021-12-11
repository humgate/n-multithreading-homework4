/**
 * Эмулятор колл-центра
 */
public class Main {
    //количество звонков которые будут сгенерированы
    static final int CALL_COUNT = 30;

    //таймаут между генерацией звонков
    static final int CALL_TIMEOUT = 1000;

    //время обработки звонка оператором
    static final int CALL_PROCESSING_TIME = 4000;

    //количество операторов
    static final int PROCESSORS_COUNT = 3;

    //время, которое оператор ждет (не отключается), в случае если очередь звонков пуста
    static final int CALL_AWAITING_TIME = 2000;

    //число, когда оператор обратился в очередь за звонком, а очередь пуста
    static final int EMPTY_QUEUE_STATES_LIMIT = 3;

    public static void main(String[] args) {
        ATC atc = new ATC();

        //начинаем генерировать звонки
        new Thread(null,()->atc.startCallGeneration(CALL_COUNT, CALL_TIMEOUT),
                "Поток-генератор").start();

        for (int i = 0; i < PROCESSORS_COUNT; i++) {
            new Thread(
                    null,
                    () -> atc.processCalls(CALL_PROCESSING_TIME,CALL_AWAITING_TIME,EMPTY_QUEUE_STATES_LIMIT),
                    "Поток-обработчик " + (i + 1)).start();
        }
    }
}
