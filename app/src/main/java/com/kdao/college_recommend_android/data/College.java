package com.kdao.college_recommend_android.data;

/**
 * Created by kdao on 10/15/16.
 */
public class College {
    private String name;
    private String image;
    private String location;
    private String tuition;
    private String acceptance_rate;
    private String ratio;
    private String total;

    public College() {}
//
//    {
//        acceptance_rate: 72.9,
//                graduation_rate: 55,
//            student_falculty_ratio: 23,
//            total_student: 36739,
//            tuition: 8959,
//            sat_score: 1510,
//            median_salary: 41000,
//            act_score: 22,
//            id: "566",
//            ranking: "N/A",
//            createdAt: "2016-10-09T19:41:33.610Z",
//            location: "San Marcos, Texas",
//            img: "Texas_State_University-San_Marcos_213524.png",
//            name: "Texas State University"
//    }
    public College(String name, String image, String location, String tuition, String acceptance_rate, String ratio, String total) {
        this.name = name;
        this.image = image;
        this.location = location;
        this.tuition = tuition;
        this.acceptance_rate = acceptance_rate;
        this.ratio = ratio;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public String getAcceptance_rate() {
        return acceptance_rate;
    }

    public void setAcceptance_rate(String acceptance_rate) {
        this.acceptance_rate = acceptance_rate;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
