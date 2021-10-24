webpackJsonp([51],{INS3:function(a,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=e("E4LH"),o={data:function(){var a=this;return{visible:!1,roleList:[],dataForm:{id:0,userName:"",password:"",comfirmPassword:"",salt:"",email:"",mobile:"",roleIdList:[],status:1},dataRule:{userName:[{required:!0,message:"用户名不能为空",trigger:"blur"}],password:[{validator:function(t,e,r){a.dataForm.id||/\S/.test(e)?r():r(new Error("密码不能为空"))},trigger:"blur"}],comfirmPassword:[{validator:function(t,e,r){a.dataForm.id||/\S/.test(e)?a.dataForm.password!==e?r(new Error("确认密码与密码输入不一致")):r():r(new Error("确认密码不能为空"))},trigger:"blur"}],email:[{required:!0,message:"邮箱不能为空",trigger:"blur"},{validator:function(a,t,e){Object(r.a)(t)?e():e(new Error("邮箱格式错误"))},trigger:"blur"}],mobile:[{required:!0,message:"手机号不能为空",trigger:"blur"},{validator:function(a,t,e){Object(r.b)(t)?e():e(new Error("手机号格式错误"))},trigger:"blur"}]}}},methods:{init:function(a){var t=this;this.dataForm.id=a||0,this.$http({url:this.$http.adornUrl("/sys/role/select"),method:"get",params:this.$http.adornParams()}).then(function(a){var e=a.data;t.roleList=e&&0===e.code?e.list:[]}).then(function(){t.visible=!0,t.$nextTick(function(){t.$refs.dataForm.resetFields()})}).then(function(){t.dataForm.id&&t.$http({url:t.$http.adornUrl("/sys/user/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(a){var e=a.data;e&&0===e.code&&(t.dataForm.userName=e.user.username,t.dataForm.salt=e.user.salt,t.dataForm.email=e.user.email,t.dataForm.mobile=e.user.mobile,t.dataForm.roleIdList=e.user.roleIdList,t.dataForm.status=e.user.status)})})},dataFormSubmit:function(){var a=this;this.$refs.dataForm.validate(function(t){t&&a.$http({url:a.$http.adornUrl("/sys/user/"+(a.dataForm.id?"update":"save")),method:"post",data:a.$http.adornData({userId:a.dataForm.id||void 0,username:a.dataForm.userName,password:a.dataForm.password,salt:a.dataForm.salt,email:a.dataForm.email,mobile:a.dataForm.mobile,status:a.dataForm.status,roleIdList:a.dataForm.roleIdList})}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.visible=!1,a.$emit("refreshDataList")}}):a.$message.error(e.msg)})})}}},s={render:function(){var a=this,t=a.$createElement,e=a._self._c||t;return e("el-dialog",{attrs:{title:a.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:a.visible},on:{"update:visible":function(t){a.visible=t}}},[e("el-form",{ref:"dataForm",attrs:{model:a.dataForm,rules:a.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&a._k(t.keyCode,"enter",13,t.key,"Enter"))return null;a.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"用户名",prop:"userName"}},[e("el-input",{attrs:{placeholder:"登录帐号"},model:{value:a.dataForm.userName,callback:function(t){a.$set(a.dataForm,"userName",t)},expression:"dataForm.userName"}})],1),a._v(" "),e("el-form-item",{class:{"is-required":!a.dataForm.id},attrs:{label:"密码",prop:"password"}},[e("el-input",{attrs:{type:"password",placeholder:"密码"},model:{value:a.dataForm.password,callback:function(t){a.$set(a.dataForm,"password",t)},expression:"dataForm.password"}})],1),a._v(" "),e("el-form-item",{class:{"is-required":!a.dataForm.id},attrs:{label:"确认密码",prop:"comfirmPassword"}},[e("el-input",{attrs:{type:"password",placeholder:"确认密码"},model:{value:a.dataForm.comfirmPassword,callback:function(t){a.$set(a.dataForm,"comfirmPassword",t)},expression:"dataForm.comfirmPassword"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"邮箱",prop:"email"}},[e("el-input",{attrs:{placeholder:"邮箱"},model:{value:a.dataForm.email,callback:function(t){a.$set(a.dataForm,"email",t)},expression:"dataForm.email"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"手机号",prop:"mobile"}},[e("el-input",{attrs:{placeholder:"手机号"},model:{value:a.dataForm.mobile,callback:function(t){a.$set(a.dataForm,"mobile",t)},expression:"dataForm.mobile"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"角色",size:"mini",prop:"roleIdList"}},[e("el-checkbox-group",{model:{value:a.dataForm.roleIdList,callback:function(t){a.$set(a.dataForm,"roleIdList",t)},expression:"dataForm.roleIdList"}},a._l(a.roleList,function(t){return e("el-checkbox",{key:t.roleId,attrs:{label:t.roleId}},[a._v(a._s(t.roleName))])}))],1),a._v(" "),e("el-form-item",{attrs:{label:"状态",size:"mini",prop:"status"}},[e("el-radio-group",{model:{value:a.dataForm.status,callback:function(t){a.$set(a.dataForm,"status",t)},expression:"dataForm.status"}},[e("el-radio",{attrs:{label:0}},[a._v("禁用")]),a._v(" "),e("el-radio",{attrs:{label:1}},[a._v("正常")])],1)],1)],1),a._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(t){a.visible=!1}}},[a._v("取消")]),a._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(t){a.dataFormSubmit()}}},[a._v("确定")])],1)],1)},staticRenderFns:[]},l=e("VU/8")(o,s,!1,null,null,null);t.default=l.exports}});