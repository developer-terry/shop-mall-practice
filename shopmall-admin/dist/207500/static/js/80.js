webpackJsonp([80],{pz5V:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o={data:function(){return{visible:!1,memberLevels:[],dataForm:{id:0,couponType:"",couponImg:"",couponName:"",num:"",amount:"",perLimit:"",minPoint:"",startTime:"",endTime:"",useType:"",note:"",publishCount:"",useCount:"",receiveCount:"",enableStartTime:"",enableEndTime:"",code:"",memberLevel:"",publish:0,timeRange:[],useTimeRange:[]},dataRule:{couponType:[{required:!0,message:"优惠卷类型不能为空",trigger:"blur"}],couponImg:[{required:!0,message:"优惠券图片不能为空",trigger:"blur"}],couponName:[{required:!0,message:"优惠卷名字不能为空",trigger:"blur"}],num:[{required:!0,message:"数量不能为空",trigger:"blur"}],amount:[{required:!0,message:"金额不能为空",trigger:"blur"}],perLimit:[{required:!0,message:"每人限领张数不能为空",trigger:"blur"}],minPoint:[{required:!0,message:"使用门槛不能为空",trigger:"blur"}],useType:[{required:!0,message:"使用类型不能为空",trigger:"blur"}],note:[{required:!0,message:"备注不能为空",trigger:"blur"}],publishCount:[{required:!0,message:"发行数量不能为空",trigger:"blur"}],enableStartTime:[{required:!0,message:"可以领取的开始日期不能为空",trigger:"blur"}],enableEndTime:[{required:!0,message:"可以领取的结束日期不能为空",trigger:"blur"}],code:[{required:!0,message:"优惠码不能为空",trigger:"blur"}],memberLevel:[{required:!0,message:"可以领取的会员等级不能为空",trigger:"blur"}]}}},created:function(){this.getMemberLevels()},methods:{getMemberLevels:function(){var e=this;this.$http({url:this.$http.adornUrl("/member/memberlevel/list"),method:"get",params:this.$http.adornParams({page:1,limit:500})}).then(function(t){var a=t.data;e.memberLevels=a.page.list})},init:function(e){var t=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/coupon/coupon/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataForm.couponType=a.coupon.couponType,t.dataForm.couponImg=a.coupon.couponImg,t.dataForm.couponName=a.coupon.couponName,t.dataForm.num=a.coupon.num,t.dataForm.amount=a.coupon.amount,t.dataForm.perLimit=a.coupon.perLimit,t.dataForm.minPoint=a.coupon.minPoint,t.dataForm.startTime=a.coupon.startTime,t.dataForm.endTime=a.coupon.endTime,t.dataForm.useType=a.coupon.useType,t.dataForm.note=a.coupon.note,t.dataForm.publishCount=a.coupon.publishCount,t.dataForm.useCount=a.coupon.useCount,t.dataForm.receiveCount=a.coupon.receiveCount,t.dataForm.enableStartTime=a.coupon.enableStartTime,t.dataForm.enableEndTime=a.coupon.enableEndTime,t.dataForm.code=a.coupon.code,t.dataForm.memberLevel=a.coupon.memberLevel,t.dataForm.publish=a.coupon.publish,t.dataForm.timeRange=[t.dataForm.startTime,t.dataForm.endTime])})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/coupon/coupon/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,couponType:e.dataForm.couponType,couponImg:e.dataForm.couponImg,couponName:e.dataForm.couponName,num:e.dataForm.num,amount:e.dataForm.amount,perLimit:e.dataForm.perLimit,minPoint:e.dataForm.minPoint,startTime:e.dataForm.useTimeRange[0],endTime:e.dataForm.useTimeRange[1],useType:e.dataForm.useType,note:e.dataForm.note,publishCount:e.dataForm.publishCount,useCount:e.dataForm.useCount,receiveCount:e.dataForm.receiveCount,enableStartTime:e.dataForm.timeRange[0],enableEndTime:e.dataForm.timeRange[1],code:e.dataForm.code,memberLevel:e.dataForm.memberLevel,publish:e.dataForm.publish})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},r={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"120px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key,"Enter"))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"优惠卷类型",prop:"couponType"}},[a("el-select",{attrs:{placeholder:"请选择"},model:{value:e.dataForm.couponType,callback:function(t){e.$set(e.dataForm,"couponType",t)},expression:"dataForm.couponType"}},[a("el-option",{attrs:{label:"全场赠券",value:0}}),e._v(" "),a("el-option",{attrs:{label:"会员赠券",value:1}}),e._v(" "),a("el-option",{attrs:{label:"购物赠券",value:2}}),e._v(" "),a("el-option",{attrs:{label:"注册赠券",value:3}})],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"优惠券图片",prop:"couponImg"}}),e._v(" "),a("el-form-item",{attrs:{label:"优惠卷名字",prop:"couponName"}},[a("el-input",{attrs:{placeholder:"优惠卷名字"},model:{value:e.dataForm.couponName,callback:function(t){e.$set(e.dataForm,"couponName",t)},expression:"dataForm.couponName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"数量",prop:"num"}},[a("el-input-number",{attrs:{min:0},model:{value:e.dataForm.num,callback:function(t){e.$set(e.dataForm,"num",t)},expression:"dataForm.num"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"金额",prop:"amount"}},[a("el-input-number",{attrs:{min:0,precision:2},model:{value:e.dataForm.amount,callback:function(t){e.$set(e.dataForm,"amount",t)},expression:"dataForm.amount"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"每人限领张数",prop:"perLimit"}},[a("el-input-number",{attrs:{min:0},model:{value:e.dataForm.perLimit,callback:function(t){e.$set(e.dataForm,"perLimit",t)},expression:"dataForm.perLimit"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"使用门槛（最小积分）",prop:"minPoint"}},[a("el-input-number",{attrs:{min:0},model:{value:e.dataForm.minPoint,callback:function(t){e.$set(e.dataForm,"minPoint",t)},expression:"dataForm.minPoint"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"有效时间",prop:"useTimeRange"}},[a("el-date-picker",{attrs:{type:"daterange","range-separator":"至","start-placeholder":"开始时间","end-placeholder":"结束时间"},model:{value:e.dataForm.useTimeRange,callback:function(t){e.$set(e.dataForm,"useTimeRange",t)},expression:"dataForm.useTimeRange"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"使用类型",prop:"useType"}},[a("el-select",{attrs:{placeholder:"请选择"},model:{value:e.dataForm.useType,callback:function(t){e.$set(e.dataForm,"useType",t)},expression:"dataForm.useType"}},[a("el-option",{attrs:{value:0,label:"全场通用"}}),e._v(" "),a("el-option",{attrs:{value:1,label:"指定分类"}}),e._v(" "),a("el-option",{attrs:{value:2,label:"指定商品"}})],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"备注",prop:"note"}},[a("el-input",{attrs:{placeholder:"备注"},model:{value:e.dataForm.note,callback:function(t){e.$set(e.dataForm,"note",t)},expression:"dataForm.note"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"发行数量",prop:"publishCount"}},[a("el-input-number",{attrs:{min:0},model:{value:e.dataForm.publishCount,callback:function(t){e.$set(e.dataForm,"publishCount",t)},expression:"dataForm.publishCount"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"领取日期",prop:"enableStartTime"}},[a("el-date-picker",{attrs:{type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:e.dataForm.timeRange,callback:function(t){e.$set(e.dataForm,"timeRange",t)},expression:"dataForm.timeRange"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"优惠码",prop:"code"}},[a("el-input",{attrs:{placeholder:"优惠码"},model:{value:e.dataForm.code,callback:function(t){e.$set(e.dataForm,"code",t)},expression:"dataForm.code"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"领取所需等级",prop:"memberLevel"}},[a("el-select",{attrs:{placeholder:"请选择"},model:{value:e.dataForm.memberLevel,callback:function(t){e.$set(e.dataForm,"memberLevel",t)},expression:"dataForm.memberLevel"}},[a("el-option",{attrs:{value:0,label:"不限制"}}),e._v(" "),e._l(e.memberLevels,function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})],2)],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},m=a("VU/8")(o,r,!1,null,null,null);t.default=m.exports}});