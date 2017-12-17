var DateKit = {
    getCnDay: function (date) {
        var str = '周';
        switch (date.getDay()) {
            case 0 :
                str += '日';
                break;
            case 1 :
                str += '一';
                break;
            case 2 :
                str += '二';
                break;
            case 3 :
                str += '三';
                break;
            case 4 :
                str += '四';
                break;
            case 5 :
                str += '五';
                break;
            case 6 :
                str += '六';
                break;
        }
        return str;
    },
    getTimeAM_PM: function (date) {
        var str = (date.getHours() % 12) + ':' + StringKit.bit(date.getMinutes(), '0');
        if (date.getHours() > 12) {
            str += ' PM';
        } else {
            str += ' AM';
        }
        return str;
    }
};

var StringKit = {
    bit: function (str, def) {
        if ((str + '').length < 2) {
            return def + str;
        }
        return str;
    }
};

var Google = {
    getIconSrc: function (icon) {
        var src = '';
        switch (icon) {
            case '01d':
            case '01n':
                src = '01';
                break;
            case '02d':
            case '02n':
                src = '02';
                break;
            case '03d':
            case '03n':
                src = '03';
                break;
            case '04d':
            case '04n':
                src = '04';
                break;
            case '09d':
            case '09n':
                src = '09';
                break;
            case '10d':
            case '10n':
                src = '10';
                break;
            case '11d':
            case '11n':
                src = '11';
                break;
            case '13d':
            case '13n':
                src = '13';
                break;
            case '50d':
            case '50n':
                src = '50';
                break;
        }
        return '/web-lte/img/' + src + '.png';
    }
};

