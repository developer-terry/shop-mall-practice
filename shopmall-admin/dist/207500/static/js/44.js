webpackJsonp([44],{TdIe:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("cdA+"),s=a("0xDb"),r={data:function(){return{updatePassowrdVisible:!1}},components:{UpdatePassword:n.default},computed:{navbarLayoutType:{get:function(){return this.$store.state.common.navbarLayoutType}},sidebarFold:{get:function(){return this.$store.state.common.sidebarFold},set:function(t){this.$store.commit("common/updateSidebarFold",t)}},mainTabs:{get:function(){return this.$store.state.common.mainTabs},set:function(t){this.$store.commit("common/updateMainTabs",t)}},userName:{get:function(){return this.$store.state.user.name}}},methods:{updatePasswordHandle:function(){var t=this;this.updatePassowrdVisible=!0,this.$nextTick(function(){t.$refs.updatePassowrd.init()})},logoutHandle:function(){var t=this;this.$confirm("确定进行[退出]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/sys/logout"),method:"post",data:t.$http.adornData()}).then(function(e){var a=e.data;a&&0===a.code&&(Object(s.a)(),t.$router.push({name:"login"}))})}).catch(function(){})}}},i={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("nav",{staticClass:"site-navbar",class:"site-navbar--"+t.navbarLayoutType},[n("div",{staticClass:"site-navbar__header"},[n("h1",{staticClass:"site-navbar__brand",on:{click:function(e){t.$router.push({name:"home"})}}},[n("a",{staticClass:"site-navbar__brand-lg",attrs:{href:"javascript:;"}},[t._v("人人快速开发平台")]),t._v(" "),n("a",{staticClass:"site-navbar__brand-mini",attrs:{href:"javascript:;"}},[t._v("人人")])])]),t._v(" "),n("div",{staticClass:"site-navbar__body clearfix"},[n("el-menu",{staticClass:"site-navbar__menu",attrs:{mode:"horizontal"}},[n("el-menu-item",{staticClass:"site-navbar__switch",attrs:{index:"0"},on:{click:function(e){t.sidebarFold=!t.sidebarFold}}},[n("icon-svg",{attrs:{name:"zhedie"}})],1)],1),t._v(" "),n("el-menu",{staticClass:"site-navbar__menu site-navbar__menu--right",attrs:{mode:"horizontal"}},[n("el-menu-item",{attrs:{index:"1"},on:{click:function(e){t.$router.push({name:"theme"})}}},[n("template",{slot:"title"},[n("el-badge",{attrs:{value:"new"}},[n("icon-svg",{staticClass:"el-icon-setting",attrs:{name:"shezhi"}})],1)],1)],2),t._v(" "),n("el-menu-item",{attrs:{index:"2"}},[n("el-badge",{attrs:{value:"hot"}},[n("a",{attrs:{href:"https://www.renren.io/",target:"_blank"}},[t._v("官方社区")])])],1),t._v(" "),n("el-submenu",{attrs:{index:"3"}},[n("template",{slot:"title"},[t._v("Git源码")]),t._v(" "),n("el-menu-item",{attrs:{index:"2-1"}},[n("a",{attrs:{href:"https://github.com/renrenio/renren-fast-vue",target:"_blank"}},[t._v("前端")])]),t._v(" "),n("el-menu-item",{attrs:{index:"2-2"}},[n("a",{attrs:{href:"https://gitee.com/renrenio/renren-fast",target:"_blank"}},[t._v("后台")])]),t._v(" "),n("el-menu-item",{attrs:{index:"2-3"}},[n("a",{attrs:{href:"https://gitee.com/renrenio/renren-generator",target:"_blank"}},[t._v("代码生成器")])])],2),t._v(" "),n("el-menu-item",{staticClass:"site-navbar__avatar",attrs:{index:"3"}},[n("el-dropdown",{attrs:{"show-timeout":0,placement:"bottom"}},[n("span",{staticClass:"el-dropdown-link"},[n("img",{attrs:{src:a("zQrT"),alt:t.userName}}),t._v(t._s(t.userName)+"\n          ")]),t._v(" "),n("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[n("el-dropdown-item",{nativeOn:{click:function(e){t.updatePasswordHandle()}}},[t._v("修改密码")]),t._v(" "),n("el-dropdown-item",{nativeOn:{click:function(e){t.logoutHandle()}}},[t._v("退出")])],1)],1)],1)],1)],1),t._v(" "),t.updatePassowrdVisible?n("update-password",{ref:"updatePassowrd"}):t._e()],1)},staticRenderFns:[]},o=a("VU/8")(r,i,!1,null,null,null);e.default=o.exports},zQrT:function(t,e,a){t.exports=a.p+"static/img/avatar.c58e465.png"}});