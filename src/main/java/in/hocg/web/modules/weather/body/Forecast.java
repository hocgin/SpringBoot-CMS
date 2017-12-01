package in.hocg.web.modules.weather.body;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
public class Forecast implements Serializable{
    
    /**
     * cod : 200
     * message : 0.0064
     * cnt : 40
     * list : [{"dt":1511362800,"main":{"temp":289.59,"temp_min":289.59,"temp_max":289.947,"pressure":1021.89,"sea_level":1033.59,"grnd_level":1021.89,"humidity":96,"temp_kf":-0.36},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":64},"wind":{"speed":5.63,"deg":17.0009},"rain":{"3h":0.085},"sys":{"pod":"n"},"dt_txt":"2017-11-22 15:00:00"},{"dt":1511373600,"main":{"temp":288.67,"temp_min":288.67,"temp_max":288.904,"pressure":1022.01,"sea_level":1033.74,"grnd_level":1022.01,"humidity":98,"temp_kf":-0.24},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03n"}],"clouds":{"all":36},"wind":{"speed":5.76,"deg":26.004},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-22 18:00:00"},{"dt":1511384400,"main":{"temp":287.98,"temp_min":287.98,"temp_max":288.102,"pressure":1022.76,"sea_level":1034.47,"grnd_level":1022.76,"humidity":100,"temp_kf":-0.12},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03n"}],"clouds":{"all":44},"wind":{"speed":6.21,"deg":27.5002},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-22 21:00:00"},{"dt":1511395200,"main":{"temp":288.271,"temp_min":288.271,"temp_max":288.271,"pressure":1024.51,"sea_level":1036.15,"grnd_level":1024.51,"humidity":97,"temp_kf":0},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"02d"}],"clouds":{"all":8},"wind":{"speed":7.46,"deg":34.0019},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-23 00:00:00"},{"dt":1511406000,"main":{"temp":290.457,"temp_min":290.457,"temp_max":290.457,"pressure":1024.8,"sea_level":1036.5,"grnd_level":1024.8,"humidity":83,"temp_kf":0},"weather":[{"id":801,"main":"Clouds","description":"few clouds","icon":"02d"}],"clouds":{"all":12},"wind":{"speed":8.06,"deg":38.0021},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-23 03:00:00"},{"dt":1511416800,"main":{"temp":291.189,"temp_min":291.189,"temp_max":291.189,"pressure":1023.03,"sea_level":1034.5,"grnd_level":1023.03,"humidity":79,"temp_kf":0},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],"clouds":{"all":36},"wind":{"speed":7.47,"deg":47.0041},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-23 06:00:00"},{"dt":1511427600,"main":{"temp":289.969,"temp_min":289.969,"temp_max":289.969,"pressure":1023.25,"sea_level":1034.69,"grnd_level":1023.25,"humidity":84,"temp_kf":0},"weather":[{"id":801,"main":"Clouds","description":"few clouds","icon":"02d"}],"clouds":{"all":20},"wind":{"speed":7.82,"deg":53.0012},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-23 09:00:00"},{"dt":1511438400,"main":{"temp":288.288,"temp_min":288.288,"temp_max":288.288,"pressure":1025.65,"sea_level":1037.15,"grnd_level":1025.65,"humidity":92,"temp_kf":0},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03n"}],"clouds":{"all":44},"wind":{"speed":7.91,"deg":45.0003},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-23 12:00:00"},{"dt":1511449200,"main":{"temp":287.772,"temp_min":287.772,"temp_max":287.772,"pressure":1026.66,"sea_level":1038.24,"grnd_level":1026.66,"humidity":94,"temp_kf":0},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],"clouds":{"all":88},"wind":{"speed":7.95,"deg":38.0014},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-23 15:00:00"},{"dt":1511460000,"main":{"temp":287.376,"temp_min":287.376,"temp_max":287.376,"pressure":1026.2,"sea_level":1037.83,"grnd_level":1026.2,"humidity":94,"temp_kf":0},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],"clouds":{"all":92},"wind":{"speed":8.27,"deg":32.5009},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-23 18:00:00"},{"dt":1511470800,"main":{"temp":286.688,"temp_min":286.688,"temp_max":286.688,"pressure":1026.22,"sea_level":1037.94,"grnd_level":1026.22,"humidity":98,"temp_kf":0},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],"clouds":{"all":88},"wind":{"speed":8.37,"deg":29.0014},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-23 21:00:00"},{"dt":1511481600,"main":{"temp":286.219,"temp_min":286.219,"temp_max":286.219,"pressure":1028.43,"sea_level":1040.22,"grnd_level":1028.43,"humidity":100,"temp_kf":0},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"clouds":{"all":92},"wind":{"speed":8.96,"deg":29.0002},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-24 00:00:00"},{"dt":1511492400,"main":{"temp":286.524,"temp_min":286.524,"temp_max":286.524,"pressure":1028.75,"sea_level":1040.46,"grnd_level":1028.75,"humidity":99,"temp_kf":0},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"clouds":{"all":92},"wind":{"speed":9.05,"deg":34.0006},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-24 03:00:00"},{"dt":1511503200,"main":{"temp":287.819,"temp_min":287.819,"temp_max":287.819,"pressure":1026.79,"sea_level":1038.48,"grnd_level":1026.79,"humidity":92,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":{"all":80},"wind":{"speed":8.26,"deg":36.0003},"rain":{"3h":0.0075},"sys":{"pod":"d"},"dt_txt":"2017-11-24 06:00:00"},{"dt":1511514000,"main":{"temp":286.823,"temp_min":286.823,"temp_max":286.823,"pressure":1026.8,"sea_level":1038.48,"grnd_level":1026.8,"humidity":99,"temp_kf":0},"weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04d"}],"clouds":{"all":68},"wind":{"speed":8.41,"deg":33.5013},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-24 09:00:00"},{"dt":1511524800,"main":{"temp":286.053,"temp_min":286.053,"temp_max":286.053,"pressure":1028.11,"sea_level":1039.94,"grnd_level":1028.11,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":92},"wind":{"speed":7.98,"deg":33.5009},"rain":{"3h":0.005},"sys":{"pod":"n"},"dt_txt":"2017-11-24 12:00:00"},{"dt":1511535600,"main":{"temp":286.064,"temp_min":286.064,"temp_max":286.064,"pressure":1028.14,"sea_level":1039.9,"grnd_level":1028.14,"humidity":100,"temp_kf":0},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],"clouds":{"all":88},"wind":{"speed":7.71,"deg":30.5005},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-24 15:00:00"},{"dt":1511546400,"main":{"temp":285.718,"temp_min":285.718,"temp_max":285.718,"pressure":1026.92,"sea_level":1038.75,"grnd_level":1026.92,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":92},"wind":{"speed":8.03,"deg":27.0022},"rain":{"3h":0.01},"sys":{"pod":"n"},"dt_txt":"2017-11-24 18:00:00"},{"dt":1511557200,"main":{"temp":285.156,"temp_min":285.156,"temp_max":285.156,"pressure":1026.16,"sea_level":1037.89,"grnd_level":1026.16,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":100},"wind":{"speed":7.15,"deg":28.0009},"rain":{"3h":0.075},"sys":{"pod":"n"},"dt_txt":"2017-11-24 21:00:00"},{"dt":1511568000,"main":{"temp":284.717,"temp_min":284.717,"temp_max":284.717,"pressure":1027.1,"sea_level":1038.88,"grnd_level":1027.1,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":{"all":92},"wind":{"speed":6.2,"deg":25.5039},"rain":{"3h":0.765},"sys":{"pod":"d"},"dt_txt":"2017-11-25 00:00:00"},{"dt":1511578800,"main":{"temp":286.442,"temp_min":286.442,"temp_max":286.442,"pressure":1026.2,"sea_level":1037.94,"grnd_level":1026.2,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":{"all":100},"wind":{"speed":6.17,"deg":34.0006},"rain":{"3h":0.54},"sys":{"pod":"d"},"dt_txt":"2017-11-25 03:00:00"},{"dt":1511589600,"main":{"temp":288.343,"temp_min":288.343,"temp_max":288.343,"pressure":1024.63,"sea_level":1036.34,"grnd_level":1024.63,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":{"all":88},"wind":{"speed":5.7,"deg":31.0005},"rain":{"3h":0.02},"sys":{"pod":"d"},"dt_txt":"2017-11-25 06:00:00"},{"dt":1511600400,"main":{"temp":288.138,"temp_min":288.138,"temp_max":288.138,"pressure":1024.44,"sea_level":1036.16,"grnd_level":1024.44,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":{"all":92},"wind":{"speed":5.13,"deg":31.5007},"rain":{"3h":0.02},"sys":{"pod":"d"},"dt_txt":"2017-11-25 09:00:00"},{"dt":1511611200,"main":{"temp":287.328,"temp_min":287.328,"temp_max":287.328,"pressure":1025.39,"sea_level":1037.2,"grnd_level":1025.39,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":100},"wind":{"speed":4.92,"deg":39.0025},"rain":{"3h":0.26},"sys":{"pod":"n"},"dt_txt":"2017-11-25 12:00:00"},{"dt":1511622000,"main":{"temp":287.138,"temp_min":287.138,"temp_max":287.138,"pressure":1025.29,"sea_level":1037.13,"grnd_level":1025.29,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":100},"wind":{"speed":4.47,"deg":37.0008},"rain":{"3h":1.17},"sys":{"pod":"n"},"dt_txt":"2017-11-25 15:00:00"},{"dt":1511632800,"main":{"temp":287.093,"temp_min":287.093,"temp_max":287.093,"pressure":1024.43,"sea_level":1036.14,"grnd_level":1024.43,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":92},"wind":{"speed":3.76,"deg":25.5084},"rain":{"3h":0.48},"sys":{"pod":"n"},"dt_txt":"2017-11-25 18:00:00"},{"dt":1511643600,"main":{"temp":287.28,"temp_min":287.28,"temp_max":287.28,"pressure":1023.71,"sea_level":1035.53,"grnd_level":1023.71,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":92},"wind":{"speed":3.21,"deg":22.001},"rain":{"3h":0.18},"sys":{"pod":"n"},"dt_txt":"2017-11-25 21:00:00"},{"dt":1511654400,"main":{"temp":286.941,"temp_min":286.941,"temp_max":286.941,"pressure":1024.76,"sea_level":1036.55,"grnd_level":1024.76,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":{"all":92},"wind":{"speed":2.61,"deg":9.50385},"rain":{"3h":0.46},"sys":{"pod":"d"},"dt_txt":"2017-11-26 00:00:00"},{"dt":1511665200,"main":{"temp":288.033,"temp_min":288.033,"temp_max":288.033,"pressure":1025.18,"sea_level":1036.94,"grnd_level":1025.18,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":{"all":92},"wind":{"speed":2.51,"deg":5.50308},"rain":{"3h":0.41},"sys":{"pod":"d"},"dt_txt":"2017-11-26 03:00:00"},{"dt":1511676000,"main":{"temp":289.167,"temp_min":289.167,"temp_max":289.167,"pressure":1023.53,"sea_level":1035.18,"grnd_level":1023.53,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":{"all":92},"wind":{"speed":3,"deg":16.0017},"rain":{"3h":0.59},"sys":{"pod":"d"},"dt_txt":"2017-11-26 06:00:00"},{"dt":1511686800,"main":{"temp":289.2,"temp_min":289.2,"temp_max":289.2,"pressure":1023.36,"sea_level":1035.08,"grnd_level":1023.36,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":{"all":92},"wind":{"speed":3.47,"deg":23.5029},"rain":{"3h":0.24},"sys":{"pod":"d"},"dt_txt":"2017-11-26 09:00:00"},{"dt":1511697600,"main":{"temp":288.837,"temp_min":288.837,"temp_max":288.837,"pressure":1025.28,"sea_level":1036.9,"grnd_level":1025.28,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":100},"wind":{"speed":4.1,"deg":25.0028},"rain":{"3h":0.53},"sys":{"pod":"n"},"dt_txt":"2017-11-26 12:00:00"},{"dt":1511708400,"main":{"temp":288.528,"temp_min":288.528,"temp_max":288.528,"pressure":1025.82,"sea_level":1037.44,"grnd_level":1025.82,"humidity":100,"temp_kf":0},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":64},"wind":{"speed":6.11,"deg":35.5032},"rain":{"3h":0.34},"sys":{"pod":"n"},"dt_txt":"2017-11-26 15:00:00"},{"dt":1511719200,"main":{"temp":288.102,"temp_min":288.102,"temp_max":288.102,"pressure":1024.97,"sea_level":1036.68,"grnd_level":1024.97,"humidity":100,"temp_kf":0},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03n"}],"clouds":{"all":44},"wind":{"speed":7.42,"deg":37.5017},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-26 18:00:00"},{"dt":1511730000,"main":{"temp":288.205,"temp_min":288.205,"temp_max":288.205,"pressure":1024.23,"sea_level":1035.83,"grnd_level":1024.23,"humidity":100,"temp_kf":0},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03n"}],"clouds":{"all":36},"wind":{"speed":8.71,"deg":39.0014},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-26 21:00:00"},{"dt":1511740800,"main":{"temp":288.463,"temp_min":288.463,"temp_max":288.463,"pressure":1025.04,"sea_level":1036.7,"grnd_level":1025.04,"humidity":95,"temp_kf":0},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"clouds":{"all":0},"wind":{"speed":9.72,"deg":36.5047},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-27 00:00:00"},{"dt":1511751600,"main":{"temp":291.17,"temp_min":291.17,"temp_max":291.17,"pressure":1025.11,"sea_level":1036.72,"grnd_level":1025.11,"humidity":85,"temp_kf":0},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"clouds":{"all":0},"wind":{"speed":9.61,"deg":40.5015},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-27 03:00:00"},{"dt":1511762400,"main":{"temp":292.757,"temp_min":292.757,"temp_max":292.757,"pressure":1022.98,"sea_level":1034.48,"grnd_level":1022.98,"humidity":81,"temp_kf":0},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"clouds":{"all":0},"wind":{"speed":8.91,"deg":44.5017},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-27 06:00:00"},{"dt":1511773200,"main":{"temp":291.347,"temp_min":291.347,"temp_max":291.347,"pressure":1022.45,"sea_level":1033.98,"grnd_level":1022.45,"humidity":86,"temp_kf":0},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"clouds":{"all":0},"wind":{"speed":8.41,"deg":43.5094},"rain":{},"sys":{"pod":"d"},"dt_txt":"2017-11-27 09:00:00"},{"dt":1511784000,"main":{"temp":289.861,"temp_min":289.861,"temp_max":289.861,"pressure":1023.62,"sea_level":1035.17,"grnd_level":1023.62,"humidity":94,"temp_kf":0},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"02n"}],"clouds":{"all":8},"wind":{"speed":8.21,"deg":37.5002},"rain":{},"sys":{"pod":"n"},"dt_txt":"2017-11-27 12:00:00"}]
     * city : {"id":1790645,"name":"Xiamen","coord":{"lat":24.4798,"lon":118.0819},"country":"CN"}
     */
    
    private String cod;
    private double message;
    private int cnt;
    private CityBean city;
    private List<ListBean> list;
    
    public String getCod() {
        return cod;
    }
    
    public void setCod(String cod) {
        this.cod = cod;
    }
    
    public double getMessage() {
        return message;
    }
    
    public void setMessage(double message) {
        this.message = message;
    }
    
    public int getCnt() {
        return cnt;
    }
    
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
    
    public CityBean getCity() {
        return city;
    }
    
    public void setCity(CityBean city) {
        this.city = city;
    }
    
    public List<ListBean> getList() {
        return list;
    }
    
    public void setList(List<ListBean> list) {
        this.list = list;
    }
    
    public static class CityBean {
        /**
         * id : 1790645
         * name : Xiamen
         * coord : {"lat":24.4798,"lon":118.0819}
         * country : CN
         */
        
        private int id;
        private String name;
        private CoordBean coord;
        private String country;
        
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
        
        public CoordBean getCoord() {
            return coord;
        }
        
        public void setCoord(CoordBean coord) {
            this.coord = coord;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public static class CoordBean {
            /**
             * lat : 24.4798
             * lon : 118.0819
             */
            
            private double lat;
            private double lon;
            
            public double getLat() {
                return lat;
            }
            
            public void setLat(double lat) {
                this.lat = lat;
            }
            
            public double getLon() {
                return lon;
            }
            
            public void setLon(double lon) {
                this.lon = lon;
            }
        }
    }
    
    public static class ListBean {
        /**
         * dt : 1511362800
         * main : {"temp":289.59,"temp_min":289.59,"temp_max":289.947,"pressure":1021.89,"sea_level":1033.59,"grnd_level":1021.89,"humidity":96,"temp_kf":-0.36}
         * weather : [{"id":500,"main":"Rain","description":"light rain","icon":"10n"}]
         * clouds : {"all":64}
         * wind : {"speed":5.63,"deg":17.0009}
         * rain : {"3h":0.085}
         * sys : {"pod":"n"}
         * dt_txt : 2017-11-22 15:00:00
         */
        
        private int dt;
        private MainBean main;
        private CloudsBean clouds;
        private WindBean wind;
        private RainBean rain;
        private SysBean sys;
        private String dt_txt;
        private List<WeatherBean> weather;
        
        public int getDt() {
            return dt;
        }
        
        public void setDt(int dt) {
            this.dt = dt;
        }
        
        public MainBean getMain() {
            return main;
        }
        
        public void setMain(MainBean main) {
            this.main = main;
        }
        
        public CloudsBean getClouds() {
            return clouds;
        }
        
        public void setClouds(CloudsBean clouds) {
            this.clouds = clouds;
        }
        
        public WindBean getWind() {
            return wind;
        }
        
        public void setWind(WindBean wind) {
            this.wind = wind;
        }
        
        public RainBean getRain() {
            return rain;
        }
        
        public void setRain(RainBean rain) {
            this.rain = rain;
        }
        
        public SysBean getSys() {
            return sys;
        }
        
        public void setSys(SysBean sys) {
            this.sys = sys;
        }
        
        public String getDt_txt() {
            return dt_txt;
        }
        
        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
        }
        
        public List<WeatherBean> getWeather() {
            return weather;
        }
        
        public void setWeather(List<WeatherBean> weather) {
            this.weather = weather;
        }
        
        public static class MainBean {
            /**
             * temp : 289.59
             * temp_min : 289.59
             * temp_max : 289.947
             * pressure : 1021.89
             * sea_level : 1033.59
             * grnd_level : 1021.89
             * humidity : 96
             * temp_kf : -0.36
             */
            
            private double temp;
            private double temp_min;
            private double temp_max;
            private double pressure;
            private double sea_level;
            private double grnd_level;
            private int humidity;
            private double temp_kf;
            
            public double getTemp() {
                return temp;
            }
            
            public void setTemp(double temp) {
                this.temp = temp;
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
            
            public double getPressure() {
                return pressure;
            }
            
            public void setPressure(double pressure) {
                this.pressure = pressure;
            }
            
            public double getSea_level() {
                return sea_level;
            }
            
            public void setSea_level(double sea_level) {
                this.sea_level = sea_level;
            }
            
            public double getGrnd_level() {
                return grnd_level;
            }
            
            public void setGrnd_level(double grnd_level) {
                this.grnd_level = grnd_level;
            }
            
            public int getHumidity() {
                return humidity;
            }
            
            public void setHumidity(int humidity) {
                this.humidity = humidity;
            }
            
            public double getTemp_kf() {
                return temp_kf;
            }
            
            public void setTemp_kf(double temp_kf) {
                this.temp_kf = temp_kf;
            }
        }
        
        public static class CloudsBean {
            /**
             * all : 64
             */
            
            private int all;
            
            public int getAll() {
                return all;
            }
            
            public void setAll(int all) {
                this.all = all;
            }
        }
        
        public static class WindBean {
            /**
             * speed : 5.63
             * deg : 17.0009
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
        
        public static class RainBean {
            /**
             * 3h : 0.085
             */
            
            @SerializedName("3h")
            private double _$3h;
            
            public double get_$3h() {
                return _$3h;
            }
            
            public void set_$3h(double _$3h) {
                this._$3h = _$3h;
            }
        }
        
        public static class SysBean {
            /**
             * pod : n
             */
            
            private String pod;
            
            public String getPod() {
                return pod;
            }
            
            public void setPod(String pod) {
                this.pod = pod;
            }
        }
        
        public static class WeatherBean {
            /**
             * id : 500
             * main : Rain
             * description : light rain
             * icon : 10n
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
}
