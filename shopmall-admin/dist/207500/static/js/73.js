webpackJsonp([73],{Vwpu:function(e,r,t){"use strict";Object.defineProperty(r,"__esModule",{value:!0});var a={data:function(){return{visible:!1,dataForm:{id:0,skuId:"",memberLevelId:"",memberLevelName:"",memberPrice:"",addOther:""},dataRule:{skuId:[{required:!0,message:"sku_id不能为空",trigger:"blur"}],memberLevelId:[{required:!0,message:"会员等级id不能为空",trigger:"blur"}],memberLevelName:[{required:!0,message:"会员等级名不能为空",trigger:"blur"}],memberPrice:[{required:!0,message:"会员对应价格不能为空",trigger:"blur"}],addOther:[{required:!0,message:"可否叠加其他优惠[0-不可叠加优惠，1-可叠加]不能为空",trigger:"blur"}]}}},methods:{init:function(e){var r=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){r.$refs.dataForm.resetFields(),r.dataForm.id&&r.$http({url:r.$http.adornUrl("/coupon/memberprice/info/"+r.dataForm.id),method:"get",params:r.$http.adornParams()}).then(function(e){var t=e.data;t&&0===t.code&&(r.dataForm.skuId=t.memberPrice.skuId,r.dataForm.memberLevelId=t.memberPrice.memberLevelId,r.dataForm.memberLevelName=t.memberPrice.memberLevelName,r.dataForm.memberPrice=t.memberPrice.memberPrice,r.dataForm.addOther=t.memberPrice.addOther)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(r){r&&e.$http({url:e.$http.adornUrl("/coupon/memberprice/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,skuId:e.dataForm.skuId,memberLevelId:e.dataForm.memberLevelId,memberLevelName:e.dataForm.memberLevelName,memberPrice:e.dataForm.memberPrice,addOther:e.dataForm.addOther})}).then(function(r){var t=r.data;t&&0===t.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(t.msg)})})}}},m={render:function(){var e=this,r=e.$createElement,t=e._self._c||r;return t("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(r){e.visible=r}}},[t("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"120px"},nativeOn:{keyup:function(r){if(!("button"in r)&&e._k(r.keyCode,"enter",13,r.key,"Enter"))return null;e.dataFormSubmit()}}},[t("el-form-item",{attrs:{label:"sku_id",prop:"skuId"}},[t("el-input",{attrs:{placeholder:"sku_id"},model:{value:e.dataForm.skuId,callback:function(r){e.$set(e.dataForm,"skuId",r)},expression:"dataForm.skuId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"会员等级id",prop:"memberLevelId"}},[t("el-input",{attrs:{placeholder:"会员等级id"},model:{value:e.dataForm.memberLevelId,callback:function(r){e.$set(e.dataForm,"memberLevelId",r)},expression:"dataForm.memberLevelId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"会员等级名",prop:"memberLevelName"}},[t("el-input",{attrs:{placeholder:"会员等级名"},model:{value:e.dataForm.memberLevelName,callback:function(r){e.$set(e.dataForm,"memberLevelName",r)},expression:"dataForm.memberLevelName"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"会员对应价格",prop:"memberPrice"}},[t("el-input",{attrs:{placeholder:"会员对应价格"},model:{value:e.dataForm.memberPrice,callback:function(r){e.$set(e.dataForm,"memberPrice",r)},expression:"dataForm.memberPrice"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"可否叠加其他优惠",prop:"addOther"}},[t("el-switch",{attrs:{"active-value":1,"inactive-value":"0","active-text":"可叠加","inactive-text":"不可叠加"},model:{value:e.dataForm.addOther,callback:function(r){e.$set(e.dataForm,"addOther",r)},expression:"dataForm.addOther"}})],1)],1),e._v(" "),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(r){e.visible=!1}}},[e._v("取消")]),e._v(" "),t("el-button",{attrs:{type:"primary"},on:{click:function(r){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},d=t("VU/8")(a,m,!1,null,null,null);r.default=d.exports}});