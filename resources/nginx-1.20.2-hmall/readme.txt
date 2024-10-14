

前端订单发送的请求http://localhost:18080/api/orders/%221837855321889366018%22
报400
多了一对双引号""


解决方法：
前端pay.html修改.replace(/\"/g, '')
// 查询订单  
axios.get("/orders/" + util.getUrlParam("id").replace(/\"/g, ''))  
  .then(resp => {  
    // 查询到订单了  
    this.order = resp.data; // 注意：确保你访问的是正确的响应数据字段，这里假设是 resp.data  
    this.tab = this.order.paymentType;  
    // 创建支付单  
    this.createPayOrder(this.order.id);  
    // 设置倒计时  
    const createTime = new Date(this.order.createTime);  
    const deadLine = createTime.getTime() + 1800000; // 30分钟后的时间戳  
    const tid = setInterval(() => {  
      const now = new Date().getTime();  
      const remainTime = deadLine - now;  
      if (remainTime > 0) {  
        this.remainTime = Math.floor(remainTime / 60000) + "分" + Math.floor((remainTime % 60000) / 1000) + "秒";  
      } else {  
        // 如果时间到了，清除倒计时并跳转到失败页  
        clearInterval(tid);  
        alert("支付超时！");  
      }  
    }, 1000); // 每秒更新一次倒计时  
  })  
  .catch(err => console.log(err));
  
失败：
解决方法*：
后端OrderController.java


@ApiOperation("根据id查询订单")
    @GetMapping("/{id}")
    public OrderVO queryOrderById(@Param ("订单id")@PathVariable("id") String id) {
        Long orderId = Long.parseLong(id.replace("\"", ""));
        return BeanUtils.copyBean(orderService.getById(orderId), OrderVO.class);
    }


