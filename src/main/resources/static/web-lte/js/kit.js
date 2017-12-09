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
    getIconSrc: function (main) {
        return '/web-lte/img/' + main + '.png';
    }
};

