package facebook_hadoop.pipeline;

import com.optimaize.langdetect.DetectedLanguage;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfileReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Utils {
    private static File jarFile = new File(
            VnCoreNLP.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    public static String jarDir = jarFile.getParentFile().getPath();

    private static LanguageDetector languageDetector = null;

    public static String detectLanguage(String text) throws IOException {
        if (languageDetector == null) {
            languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .shortTextAlgorithm(0)
                    .withProfiles(new LanguageProfileReader().readAllBuiltIn())
                    .build();
        }
        List<DetectedLanguage> detectedLanguages = languageDetector.getProbabilities(text);
        if (detectedLanguages.size() > 0)
            return detectedLanguages.get(0).getLocale().getLanguage();
        return "N/A";
    }

}
