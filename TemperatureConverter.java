import java.util.Scanner;

/** A simple utility for converting temperatures between Celsius, Fahrenheit, and Kelvin. */
public class TemperatureConverter {

    public static double convertCelsius(double celsius, String targetScale) {
        validateTemperature(celsius, "C");
        switch (normalizeScale(targetScale)) {
            case "F": return celsius * 9.0 / 5.0 + 32.0;
            case "K": return celsius + 273.15;
            case "C": return celsius;
            default: throw new IllegalArgumentException("Invalid target scale.");
        }
    }

    public static double convertFahrenheit(double fahrenheit, String targetScale) {
        validateTemperature(fahrenheit, "F");
        switch (normalizeScale(targetScale)) {
            case "C": return (fahrenheit - 32.0) * 5.0 / 9.0;
            case "K": return (fahrenheit - 32.0) * 5.0 / 9.0 + 273.15;
            case "F": return fahrenheit;
            default: throw new IllegalArgumentException("Invalid target scale.");
        }
    }

    public static double convertKelvin(double kelvin, String targetScale) {
        validateTemperature(kelvin, "K");
        switch (normalizeScale(targetScale)) {
            case "C": return kelvin - 273.15;
            case "F": return (kelvin - 273.15) * 9.0 / 5.0 + 32.0;
            case "K": return kelvin;
            default: throw new IllegalArgumentException("Invalid target scale.");
        }
    }

    /** Overload 1: convert one temperature. */
    public static double convertMultiple(double temperature, String sourceScale, String targetScale) {
        String source = normalizeScale(sourceScale);
        switch (source) {
            case "C": return convertCelsius(temperature, targetScale);
            case "F": return convertFahrenheit(temperature, targetScale);
            case "K": return convertKelvin(temperature, targetScale);
            default: throw new IllegalArgumentException("Invalid source scale.");
        }
    }

    /** Overload 2: convert a whole array in one method call. */
    public static double[] convertMultiple(double[] temperatures, String sourceScale, String targetScale) {
        if (temperatures == null) {
            throw new IllegalArgumentException("Temperature array cannot be null.");
        }
        double[] results = new double[temperatures.length];
        for (int i = 0; i < temperatures.length; i++) {
            results[i] = convertMultiple(temperatures[i], sourceScale, targetScale);
        }
        return results;
    }

    private static String normalizeScale(String scale) {
        if (scale == null) {
            throw new IllegalArgumentException("Scale cannot be null.");
        }
        String normalized = scale.trim().toUpperCase();
        if (!normalized.equals("C") && !normalized.equals("F") && !normalized.equals("K")) {
            throw new IllegalArgumentException("Scale must be C, F, or K.");
        }
        return normalized;
    }

    private static void validateTemperature(double temperature, String scale) {
        if (!Double.isFinite(temperature)) {
            throw new IllegalArgumentException("Temperature must be a finite number.");
        }
        double kelvin;
        switch (scale) {
            case "C": kelvin = temperature + 273.15; break;
            case "F": kelvin = (temperature - 32.0) * 5.0 / 9.0 + 273.15; break;
            default: kelvin = temperature;
        }
        if (kelvin < -1e-9) {
            throw new IllegalArgumentException("Temperature cannot be below absolute zero (0 K).");
        }
    }

    /** Java passes a copy of the primitive double, so this cannot modify the caller's value. */
    private static void demonstratePassByValue(double value) {
        value = value + 10;
        System.out.printf("Inside method: %.2f%n", value);
    }

    public static void main(String[] args) {
        System.out.println("=== Temperature Converter ===");
        System.out.printf("25 C is equivalent to %.2f F and %.2f K%n",
                convertCelsius(25, "F"), convertCelsius(25, "K"));
        System.out.printf("68 F is equivalent to %.2f C and %.2f K%n",
                convertFahrenheit(68, "C"), convertFahrenheit(68, "K"));
        System.out.printf("300 K is equivalent to %.2f C and %.2f F%n",
                convertKelvin(300, "C"), convertKelvin(300, "F"));

        System.out.println("\nBatch conversion (C to F):");
        double[] temperatures = {0, 25, 100};
        double[] converted = convertMultiple(temperatures, "C", "F");
        for (int i = 0; i < temperatures.length; i++) {
            System.out.printf("%.2f C -> %.2f F%n", temperatures[i], converted[i]);
        }

        System.out.println("\nPass-by-value example:");
        double original = 25;
        demonstratePassByValue(original);
        System.out.printf("After method call, original is still %.2f%n", original);

        // Accept a custom temperature and catch invalid entries without crashing.
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("\nTry your own conversion.");
            System.out.print("Enter temperature: ");
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input: please enter a number.");
                return;
            }
            double temperature = scanner.nextDouble();
            System.out.print("Source scale (C/F/K): ");
            String source = scanner.next();
            System.out.print("Target scale (C/F/K): ");
            String target = scanner.next();
            try {
                double result = convertMultiple(temperature, source, target);
                System.out.printf("%.2f %s = %.2f %s%n", temperature,
                        normalizeScale(source), result, normalizeScale(target));
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid input: " + ex.getMessage());
            }
        }
    }
}
