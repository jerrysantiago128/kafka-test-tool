package msps.test.kafka;

import msps.test.kafka.Producer;
import msps.test.kafka.Consumer;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {  // Need at least mode (producer/consumer) and other arguments
            System.err.println("Usage: java -jar your-jar.jar [producer|consumer] <arguments...>");
            System.exit(1);
        }

        String mode = args[0];
        String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);

        try {
            if (mode.equals("producer")) {
                Producer.main(remainingArgs);
            } else if (mode.equals("consumer")) {
                Consumer.main(remainingArgs);
            } else {
                System.err.println("Invalid mode.  Use 'producer' or 'consumer'.");
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
