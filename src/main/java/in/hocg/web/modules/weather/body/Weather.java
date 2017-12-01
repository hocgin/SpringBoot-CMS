package in.hocg.web.modules.weather.body;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
public class Weather implements Serializable {
    
    /**
     * coord : {"lon":118.08,"lat":24.48}
     * weather : [{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}]
     * base : stations
     * main : {"temp":293.15,"pressure":1018,"humidity":60,"temp_min":293.15,"temp_max":293.15}
     * visibility : 10000
     * wind : {"speed":3,"deg":10}
     * clouds : {"all":90}
     * dt : 1511355600
     * sys : {"type":1,"id":7438,"message":0.0139,"country":"CN","sunrise":1511303311,"sunset":1511342346}
     * id : 1790645
     * name : Xiamen
     * cod : 200
     */
    
    private CoordBean coord;
    private String base;
    private MainBean main;
    private int visibility;
    private WindBean wind;
    private CloudsBean clouds;
    private long dt;
    private SysBean sys;
    private int id;
    private String name;
    private int cod;
    private List<WeatherBean> weather;
    
    public CoordBean getCoord() {
        return coord;
    }
    
    public void setCoord(CoordBean coord) {
        this.coord = coord;
    }
    
    public String getBase() {
        return base;
    }
    
    public void setBase(String base) {
        this.base = base;
    }
    
    public MainBean getMain() {
        return main;
    }
    
    public void setMain(MainBean main) {
        this.main = main;
    }
    
    public int getVisibility() {
        return visibility;
    }
    
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
    
    public WindBean getWind() {
        return wind;
    }
    
    public void setWind(WindBean wind) {
        this.wind = wind;
    }
    
    public CloudsBean getClouds() {
        return clouds;
    }
    
    public void setClouds(CloudsBean clouds) {
        this.clouds = clouds;
    }
    
    public long getDt() {
        return dt;
    }
    
    public void setDt(long dt) {
        this.dt = dt;
    }
    
    public SysBean getSys() {
        return sys;
    }
    
    public void setSys(SysBean sys) {
        this.sys = sys;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getCod() {
        return cod;
    }
    
    public void setCod(int cod) {
        this.cod = cod;
    }
    
    public List<WeatherBean> getWeather() {
        return weather;
    }
    
    public void setWeather(List<WeatherBean> weather) {
        this.weather = weather;
    }
    
    public static class CoordBean {
        /**
         * lon : 118.08
         * lat : 24.48
         */
        
        private double lon;
        private double lat;
        
        public double getLon() {
            return lon;
        }
        
        public void setLon(double lon) {
            this.lon = lon;
        }
        
        public double getLat() {
            return lat;
        }
        
        public void setLat(double lat) {
            this.lat = lat;
        }
    }
    
    public static class MainBean {
        /**
         * temp : 293.15
         * pressure : 1018
         * humidity : 60
         * temp_min : 293.15
         * temp_max : 293.15
         */
        
        private double temp;
        private double pressure;
        private double humidity;
        private double temp_min;
        private double temp_max;
        
        public double getTemp() {
            return temp;
        }
        
        public void setTemp(double temp) {
            this.temp = temp;
        }
        
        public double getPressure() {
            return pressure;
        }
        
        public void setPressure(double pressure) {
            this.pressure = pressure;
        }
        
        public double getHumidity() {
            return humidity;
        }
        
        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }
        
        public double getTemp_min() {
            return temp_min;
        }
        
        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }
        
        public double getTemp_max() {
            return temp_max;
        }
        
        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }
    }
    
    public static class WindBean {
        /**
         * speed : 3
         * deg : 10
         */
        
        private double speed;
        private double deg;
        
        public double getSpeed() {
            return speed;
        }
        
        public void setSpeed(double speed) {
            this.speed = speed;
        }
        
        public double getDeg() {
            return deg;
        }
        
        public void setDeg(double deg) {
            this.deg = deg;
        }
    }
    
    public static class CloudsBean {
        /**
         * all : 90
         */
        
        private int all;
        
        public int getAll() {
            return all;
        }
        
        public void setAll(int all) {
            this.all = all;
        }
    }
    
    public static class SysBean {
        /**
         * type : 1
         * id : 7438
         * message : 0.0139
         * country : CN
         * sunrise : 1511303311
         * sunset : 1511342346
         */
        
        private int type;
        private int id;
        private double message;
        private String country;
        private int sunrise;
        private int sunset;
        
        public int getType() {
            return type;
        }
        
        public void setType(int type) {
            this.type = type;
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public double getMessage() {
            return message;
        }
        
        public void setMessage(double message) {
            this.message = message;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public int getSunrise() {
            return sunrise;
        }
        
        public void setSunrise(int sunrise) {
            this.sunrise = sunrise;
        }
        
        public int getSunset() {
            return sunset;
        }
        
        public void setSunset(int sunset) {
            this.sunset = sunset;
        }
    }
    
    public static class WeatherBean {
        /**
         * id : 804
         * main : Clouds
         * description : overcast clouds
         * icon : 04n
         */
        
        private int id;
        private String main;
        private String description;
        private String icon;
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public String getMain() {
            return main;
        }
        
        public void setMain(String main) {
            this.main = main;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getIcon() {
            return icon;
        }
        
        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
