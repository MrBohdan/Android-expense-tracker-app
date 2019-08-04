package models;

public class Settings {

    private int id;
    private int user_ID;
    private boolean pieChart;
    private boolean barChart;
    private boolean radarChart;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public boolean isPieChart() {
        return pieChart;
    }

    public void setPieChart(boolean pieChart) {
        this.pieChart = pieChart;
    }

    public boolean isBarChart() {
        return barChart;
    }

    public void setBarChart(boolean barChart) {
        this.barChart = barChart;
    }

    public boolean isRadarChart() {
        return radarChart;
    }

    public void setRadarChart(boolean radarChart) {
        this.radarChart = radarChart;
    }
}
