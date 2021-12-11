import java.util.concurrent.ConcurrentLinkedQueue;

public class ATC {
    //быстро работает, реализует FIFO, что еще нужно
    private final ConcurrentLinkedQueue<Call> queue = new ConcurrentLinkedQueue<>();

    /**
     * Запускает процесс генерации звонков
     * @param callCount - число звонков для генерации
     * @param callTimeOut - таймаут между генерациями звонков
     */
    public void startCallGeneration(int callCount, int callTimeOut) {
        for (int i = 0; i <callCount ; i++) {
            queue.add(new Call());
            System.out.println(
                    Thread.currentThread().getName()+ " сгенерировал звонков: " + (i + 1));
            try {
                Thread.sleep(callTimeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Запускает процесс обработки звонков из очереди. Работает до тех пор, пока количество ситуаций, когда очередь
     * пуста при попытке обработать звонок, не превысит заданное значение.
     * @param processingTime - время обработки звонка
     * @param awaitingTime - время ожидания, если очередь звонков пуста
     * @param emptyQueueStatesLimit - количество ситуаций когда очередь пуста до отключения
     */
    public void processCalls(int processingTime, int awaitingTime, int emptyQueueStatesLimit) {
        //счетчик обработанных данным потоком-оператором звонков
        ThreadLocal<Integer> processedCalls = ThreadLocal.withInitial(() -> 0);

        //счетчик ситуаций когда поток-обработчик обнаруживает пустую очередь
        ThreadLocal<Integer> emptyQueueStates = ThreadLocal.withInitial(() -> 0);

        System.out.println(Thread.currentThread().getName() + " подключился работе");

        //поток-оператор не прекращает работу пока не обнаружит пустую очередь заданное число раз
        while (emptyQueueStates.get() < emptyQueueStatesLimit) {
            //Если очередь не пуста, обрабатывает звонок
            if (queue.poll() != null) {
                try {
                    Thread.sleep(processingTime);
                    processedCalls.set(processedCalls.get() + 1);
                    System.out.println(
                            Thread.currentThread().getName() + " обработал звонков: " + processedCalls.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            //Если очередь пуста, инкрементим счетчик таких ситуаций
            } else {
                try {
                    //ждет появления звонков в очереди
                    Thread.sleep(awaitingTime);
                    emptyQueueStates.set(emptyQueueStates.get() + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + " отключился от обработки");
    }
}
