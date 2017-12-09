(function (a) {
    (jQuery.browser = jQuery.browser || {}).mobile = /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4));
})(navigator.userAgent || navigator.vendor || window.opera);
$.ajaxSetup({
    contentType: 'application/x-www-form-urlencoded;charset=utf-8',
    complete: function (XMLHttpRequest, textStatus) {
        var sessionstatus = XMLHttpRequest.getResponseHeader('loginStatus'); //通过XMLHttpRequest取得响应头，sessionstatus，
        if (sessionstatus === 'accessDenied') {
            layer.msg("\u767b\u5f55\u5931\u6548\uff0c\u8bf7\u5237\u65b0\u9875\u9762\u91cd\u65b0\u767b\u5f55");
        }
        if (sessionstatus === 'unauthorized') {
            layer.msg("\u6ca1\u6709\u6743\u9650");
        }
    }
});

/**
 * 工具类
 * @type {{removeHTMLTag: Utils.removeHTMLTag}}
 */
const LangUtils = {
    removeHTMLTag: function (str) {
        str = str.replace(/<\/?[^>]*>/g, ''); //去除HTML tag
        str = str.replace(/[ | ]*\n/g, '\n'); //去除行尾空白
        str = str.replace(/\n[\s| | ]*\r/g, '\n'); //去除多余空行
        str = str.replace(/&nbsp;/ig, '');//去掉&nbsp;
        return str;
    },
    bytesToSize: function (bytes) {
        if (bytes === 0) return '0 B';
        var k = 1000, // or 1024
            sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            i = Math.floor(Math.log(bytes) / Math.log(k));

        return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
    }
};


const DATA_TABLE_CONFIG = {
    language: {    // 语言设置
        info: "结果: _START_ ~ _END_ , 共 _TOTAL_ 条",
        infoEmpty: "没有数据",
        infoFiltered: "(从 _MAX_ 条数据中检索)",
        zeroRecords: "没有检索到数据",
        search: "搜索:",
        paginate: {
            sFirst: "首页",
            sPrevious: "前一页",
            sNext: "后一页",
            sLast: "尾页"
        }
    },
    preDrawCallback: function () {
        layer.msg('加载中');
    }
};

UploadBox.prototype = {
    id: undefined,
    $textSub: undefined,
    $uploadArea: undefined, // 上传显示信息
    uploader: undefined,
    option: undefined,
    input: undefined,
    count: 0, // 当前上传的数量
    init: function (option) {
        var that = this;
        this.option = option;
        this.count = option.count;
        this.input = option.input;
        this.id = option.id;
        this.$uploadArea = $(this.id + ' .upload-area');
        this.$textSub = $(this.id + ' .text-sub');

        this.uploader = WebUploader.create({
            auto: true,
            swf: option.swf || '/static/dist/webuploader/Uploader.swf',
            server: option.server || 'http://localhost:8080/public/upload',
            pick: {
                id: this.id + ' .upload-btn',
                multiple: true
            },
            accept: option.accept,
            resize: true
        }).on('uploadBeforeSend', function (object, data, headers) {
            headers[option.header] = option.token;
        }).on('fileQueued', function (file) {
            if (!!that.option.maxCount
                && that.count >= that.option.maxCount) {
                layer.msg('上传文件数量已到达限制', {icon: 2});
                return;
            }
            that.$textSub.hide();
            that.$uploadArea.append(that.getUploadProgressHtml(file));
            var $this = $(that.id + ' .' + file.id);
            $this.find('.operating')
                .not('.cancel-upload').hide();
            that.count++;
        }).on('uploadProgress', function (file, percentage) {
            // 上传中
            var $this = $(that.id + ' .' + file.id);
            that.progress($this, percentage);
        }).on('fileDequeued', function () {
            that.count--;
        }).on('uploadError', function (file, reason) {
            // 上传出错
            var $this = $(that.id + ' .' + file.id);
            that.error($this, reason);
        }).on('uploadSuccess', function (file, data) {
            var $this = $(that.id + ' .' + file.id);
            if (data.code === 200) {
                $this.data('fid', data.data.id);
                that.success($this);
                $this.find('.file-name').attr('title', data.data.id);
                that.resetFilesId();
            } else if (data.code === 220) {
                that.exits($this);
            } else {
                that.error($this, data.message);
            }
        });
        // 初始化设置值
        that.resetFilesId();

        /**
         * 取消上传, 仅从队列中移除
         */
        this.$uploadArea.on('click', '.cancel-upload', function () {
            var id = $(this).data('id');
            that.cancelFile(id);
            that.resetFilesId();
        });
        /**
         * 删除上传文件, 仅从队列中移除, ~~从服务器上移除~~
         * > 不应该实时删除, 由服务器决定
         */
        this.$uploadArea.on('click', '.remove-upload', function () {
            var id = $(this).data('id');
            that.cancelFile(id);
            that.resetFilesId();
        });
        return this;
    },
    getUploadProgressHtml: function (file) {
        return '<div class="upload-progress ' + file.id + '">\n' +
            '                        <div class="progress-info">\n' +
            '                            <a href="javascript:;;" class="operating cancel-upload" data-id="' + file.id + '">\n' +
            '                                <i class="glyphicon glyphicon-ban-circle"></i>\n' +
            '                            </a>\n' +
            '                            <span class="operating success-upload">\n' +
            '                                <i class="glyphicon glyphicon-ok"></i>\n' +
            '                            </span>\n' +
            '                            <a href="javascript:;;" class="operating remove-upload" data-id="' + file.id + '">\n' +
            '                                <i class="glyphicon glyphicon-trash"></i>\n' +
            '                            </a>\n' +
            '                            <span class="file-info">\n' +
            '                                <span title="' + file.name + '" class="file-name">' + file.name + '</span>\n' +
            '                                <span class="file-size">' + this.formatBytes(file.size) + '</span>\n' +
            '                            </span>\n' +
            '                            <span class="upload-status"></span>\n' +
            '                        </div>\n' +
            '                        <div class="progress-bg" style="width: 0%;"></div>\n' +
            '                    </div>';
    },
    formatBytes: function (bytes) {
        if (bytes === 0) return '0 B';
        var k = 1024, // or 1024
            sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            i = Math.floor(Math.log(bytes) / Math.log(k));

        return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
    },
    error: function (uploadProgress, reason) {
        var $this = $(uploadProgress);
        $this.find('.operating').show()
            .not('.cancel-upload').hide();
        $this.find('.progress-bg').addClass('error')
            .css('width', '100%');
        $this.find('.file-size').text('错误: ' + reason);
    },
    exits: function (uploadProgress) {
        var $this = $(uploadProgress);
        $this.find('.operating').show()
            .not('.cancel-upload').hide();
        $this.find('.progress-bg').addClass('exist')
            .css('width', '100%');
        $this.find('.file-size').text('文件已存在');
        $this.find('.upload-status').text('');
    },
    success: function (uploadProgress) {
        var $this = $(uploadProgress);
        $this.find('.operating').show()
            .not('.success-upload,.remove-upload').hide();
        this.progress(uploadProgress, 100);
        $this.find('.progress-bg').addClass("success");
    },
    progress: function (uploadProgress, percentage) {
        var $this = $(uploadProgress);
        var value = percentage + '%';
        $this.find('.progress-bg').css('width', value);
        $this.find('.upload-status').text(value);
    },
    cancelFile: function (id) {
        $(this.id + ' .' + id).remove();
        if ((id + '').indexOf('WU_FILE') === 0) {
            this.uploader.cancelFile(id, true);
        }
        this.count--;
        if ($(this.id + ' .upload-progress').length === 0) {
            this.$textSub.show();
        }
    },
    resetFilesId: function () {
        var ids = [];
        $.each($(this.id + ' .upload-progress'), function (i, e) {
            var $e = $(e);
            ids.push($e.data('fid'));
        });
        $(this.input).val(ids.toString());
    }
};

function UploadBox(option) {
    var ub = {};
    ub.__proto__ = UploadBox.prototype;
    ub.init(option);
    return ub;
}

var DATETIME_PICKER_CONFIG = {
    language: 'zh-CN',
    format: 'yyyy-mm-dd hh:ii:ss',
    weekStart: 1,
    todayBtn: 1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    showMeridian: 1
};
$.getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};