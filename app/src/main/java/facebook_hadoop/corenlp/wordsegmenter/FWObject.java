package facebook_hadoop.corenlp.wordsegmenter;

public class FWObject {
    private String[] context;

    public FWObject(boolean check) {
        context = new String[10];
        if (check == true) {
            for (int i = 0; i < 10; i += 2) {
                context[i] = "<W>";
                context[i + 1] = "<T>";
            }
        }
    }

    public String[] getContext() {
        return context;
    }

    public void setContext(String[] context) {
        this.context = context;
    }
}
