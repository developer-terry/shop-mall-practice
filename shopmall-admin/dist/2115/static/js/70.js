webpackJsonp([70],{"79vE":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,memberId:"",skuId:"",sessionId:"",subcribeTime:"",sendTime:"",noticeType:""},dataRule:{memberId:[{required:!0,message:"member_id不能为空",trigger:"blur"}],skuId:[{required:!0,message:"sku_id不能为空",trigger:"blur"}],sessionId:[{required:!0,message:"活动场次id不能为空",trigger:"blur"}],subcribeTime:[{required:!0,message:"订阅时间不能为空",trigger:"blur"}],sendTime:[{required:!0,message:"发送时间不能为空",trigger:"blur"}],noticeType:[{required:!0,message:"通知方式不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/coupon/seckillskunotice/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataForm.memberId=a.seckillSkuNotice.memberId,t.dataForm.skuId=a.seckillSkuNotice.skuId,t.dataForm.sessionId=a.seckillSkuNotice.sessionId,t.dataForm.subcribeTime=a.seckillSkuNotice.subcribeTime,t.dataForm.sendTime=a.seckillSkuNotice.sendTime,t.dataForm.noticeType=a.seckillSkuNotice.noticeType)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/coupon/seckillskunotice/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,memberId:e.dataForm.memberId,skuId:e.dataForm.skuId,sessionId:e.dataForm.sessionId,subcribeTime:e.dataForm.subcribeTime,sendTime:e.dataForm.sendTime,noticeType:e.dataForm.noticeType})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},i={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"120px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key,"Enter"))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"member_id",prop:"memberId"}},[a("el-input",{attrs:{placeholder:"member_id"},model:{value:e.dataForm.memberId,callback:function(t){e.$set(e.dataForm,"memberId",t)},expression:"dataForm.memberId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"sku_id",prop:"skuId"}},[a("el-input",{attrs:{placeholder:"sku_id"},model:{value:e.dataForm.skuId,callback:function(t){e.$set(e.dataForm,"skuId",t)},expression:"dataForm.skuId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"活动场次id",prop:"sessionId"}},[a("el-input",{attrs:{placeholder:"活动场次id"},model:{value:e.dataForm.sessionId,callback:function(t){e.$set(e.dataForm,"sessionId",t)},expression:"dataForm.sessionId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"订阅时间",prop:"subcribeTime"}},[a("el-input",{attrs:{placeholder:"订阅时间"},model:{value:e.dataForm.subcribeTime,callback:function(t){e.$set(e.dataForm,"subcribeTime",t)},expression:"dataForm.subcribeTime"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"发送时间",prop:"sendTime"}},[a("el-input",{attrs:{placeholder:"发送时间"},model:{value:e.dataForm.sendTime,callback:function(t){e.$set(e.dataForm,"sendTime",t)},expression:"dataForm.sendTime"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"通知方式",prop:"noticeType"}},[a("el-select",{attrs:{placeholder:"请选择"},model:{value:e.dataForm.noticeType,callback:function(t){e.$set(e.dataForm,"noticeType",t)},expression:"dataForm.noticeType"}},[a("el-option",{attrs:{label:"短信",value:0}}),e._v(" "),a("el-option",{attrs:{label:"邮件",value:1}})],1)],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},s=a("VU/8")(r,i,!1,null,null,null);t.default=s.exports}});