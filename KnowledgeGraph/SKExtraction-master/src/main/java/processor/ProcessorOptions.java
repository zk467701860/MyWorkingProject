package processor;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

/**
 * Options handles the StnfordPipelineUtil settings which should be loaded out of a configuration file.
 */
public class ProcessorOptions {
    public static final String UNIVERSAL_DEPENDENCY_TYPE_BASIC = "basic";
    public static final String UNIVERSAL_DEPENDENCY_TYPE_ENHANCED = "enhanced";
    public static final String UNIVERSAL_DEPENDENCY_TYPE_ENHANCED_PLUSPLUS = "enhanced++";

    //if true,use the Stanford NLP neuralNetworkDependencyParser,false,use the Stanford NLP LexicalizedParser instead.
    public boolean neuralNetworkDependencyParser;

    public String UniversalDependencyType;

    /**
     * Constructs the set of options out of a conf file (clausie.conf)
     */
    public ProcessorOptions() {
        try {
            InputStream in = getClass().getResource("resources/processor.conf").openStream();
            setOptions(in);
            in.close();
        } catch (IOException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs the set of options out of a conf file (fileOrResourceName)
     */
    public ProcessorOptions(String fileOrResourceName) throws IOException {
        InputStream in = openFileOrResource(fileOrResourceName);
        setOptions(in);
        in.close();
    }

    private InputStream openFileOrResource(String name) throws IOException {
        try {
            File file = new File(name);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
        }
        URL url = getClass().getResource(name);
        if (url == null) {
            throw new IOException("File or resource '" + name + "' not found.");
        }
        return url.openStream();
    }

    /**
     * Load options from the configuration file
     */
    public void setOptions(InputStream optionsStream) throws IOException {
        Properties prop = new Properties();
        prop.load(optionsStream);

        neuralNetworkDependencyParser = Boolean.parseBoolean(getProperty(prop, "neuralNetworkDependencyParser"));
        UniversalDependencyType = getProperty(prop, "UniversalDependencyType");
        if (!UniversalDependencyType.equals(UNIVERSAL_DEPENDENCY_TYPE_BASIC) && !UniversalDependencyType.equals(UNIVERSAL_DEPENDENCY_TYPE_ENHANCED) && !UniversalDependencyType.equals(UNIVERSAL_DEPENDENCY_TYPE_ENHANCED_PLUSPLUS)) {
            System.err.println("Unknown option value for UniversalDependencyType. \n"
                    + "must be one of basic,enhanced,enhanced++\n" +
                    "set to basic by default");
            UniversalDependencyType = UNIVERSAL_DEPENDENCY_TYPE_BASIC;
        }

        // check for unused properties
        if (!prop.isEmpty()) {
            System.err.println("Unknown option(s): "
                    + Arrays.toString(prop.keySet().toArray()));
        }
    }

    /**
     * Returns a required option (key)
     */
    private String getProperty(Properties prop, String key) throws IOException {
        String result = prop.getProperty(key);
        if (result == null) {
            throw new IOException("Missing option: " + key);
        }
        prop.remove(key);
        return result;
    }

    public void print(OutputStream out) {
        print(out, "");
    }

    /**
     * Print settings
     */
    public void print(OutputStream out, String prefix) {
        PrintStream pout = new PrintStream(out);
        pout.println(prefix + "  use Neural Network Dependency Parser     : " + neuralNetworkDependencyParser);
        if (!neuralNetworkDependencyParser)
            pout.println(prefix + "  use PCFG and lexicalized dependency parsers     : True");
        pout.println(prefix + "  Universal Dependency Type     : \"" + UniversalDependencyType + "\"");
    }
}
