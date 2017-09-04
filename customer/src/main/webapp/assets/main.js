/**
 * Created by dev on 2017/4/27.
 */

var url = "http://127.0.0.1:8080/customer/api/";

function loginOut(){
    $.ajax({
        "async": true,
        "crossDomain": true,
        "url": url+"user/loginOut",
        "method": "GET",
        "headers": {
            "content-type": "application/json",
            "cache-control": "no-cache",
            "postman-token": "f7b21967-8095-8bea-5f60-ed8da73aaa16"
        },
        success:function(data){
            console.log(data);
            deleteCookie("personMessage","/");
            location.href = "login.html";
        },
        error:function(data){
            console.log("login out error");
            location.href = "login.html";
        }
    })
}

//毫秒转格式时间
Date.prototype.toLocaleString = function() {
    return this.getFullYear() + "年" + (this.getMonth() + 1) + "月" + this.getDate() + "日  " + change(this.getHours()) + ":" + change(this.getMinutes()) + ":" + change(this.getSeconds());
};

function change(i){
    var l=i.toString().length;
    if(l==1){
        i = "0"+i;
    }
    return i;
}

function deleteCookie(name,path){   /**根据cookie的键，删除cookie，其实就是设置其失效**/
var name = escape(name);
    var expires = new Date(0);
    path = path == "" ? "" : ";path=" + path;
    document.cookie = name + "="+ ";expires=" + expires.toUTCString() + path;
}

//保存cookie
function addCookie(name,value,days,path){   /**添加设置cookie**/
var name = escape(name);
    var value = escape(value);
    var expires = new Date();
    expires.setTime(expires.getTime() + days * 3600000 * 24);
    //path=/，表示cookie能在整个网站下使用，path=/temp，表示cookie只能在temp目录下使用
    path = path == "" ? "" : ";path=" + path;
    //GMT(Greenwich Mean Time)是格林尼治平时，现在的标准时间，协调世界时是UTC
    //参数days只能是数字型
    var _expires = (typeof days) == "string" ? "" : ";expires=" + expires.toUTCString();
    document.cookie = name + "=" + value + _expires + path;
}
function handleAjaxError( xhr, textStatus) {
    if ( textStatus === 'timeout' ) {
        alert( 'The server took too long to send the data.' );
    }
    if(xhr.status==403||xhr.status==401||xhr.status==400){
        alert("您没有权限操作该页面");
    }
}