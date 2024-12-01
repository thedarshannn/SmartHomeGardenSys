public class Photo {
    private String id;
    private String url;
    private String name;
    private String date;

    // Constructor
    public Photo(String id, String url, String name, String date) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.date = date;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
