package assfinder.model;

public class AssField {
    private String keyword;
    private int keywordfreq;
    private String ass;
    private int assfreq;

    public AssField(String keyword, int keywordfreq, String ass, int assfreq) {
        this.keyword = keyword;
        this.keywordfreq = keywordfreq;
        this.ass = ass;
        this.assfreq = assfreq;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getKeywordfreq() {
        return keywordfreq;
    }

    public void setKeywordfreq(int keywordfreq) {
        this.keywordfreq = keywordfreq;
    }

    public String getAss() {
        return ass;
    }

    public void setAss(String ass) {
        this.ass = ass;
    }

    public int getAssfreq() {
        return assfreq;
    }

    public void setAssfreq(int assfreq) {
        this.assfreq = assfreq;
    }
}
