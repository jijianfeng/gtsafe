<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div class="preview-box">
	<div class="left">
		<div class="p-box time tc">
			<div class="bd">
				<div class="t1">
					安全生产第<em class="number1 red" id="the_day">0</em>日
				</div>
				<div class="t2">
					<span id="get_time">0000-00-00 00:00:00</span> <span>&nbsp;&nbsp;倒计时:<em
						class="number2 orange" id="count_down">00</em>天</span>
				</div>
			</div>
		</div>
		<div class="p-box">
			<div class="hd">安全生产方针、目标</div>
			<div class="bd" style="height: 87px;">${config.policy }</div>
		</div>
		<div class="p-box">
			<div class="hd">安全生产管理体系</div>
			<div class="bd system" style="height: 100px;">
				${config.message }
			</div>
		</div>
		<div class="p-box">
			<div class="hd">布告栏</div>
			<div class="bd" style="height: 100px;overflow: hidden;">
				<div id="preview_notic">
					<ul>
						<c:forEach items="${notice}" var="notice">
							<li>
								<div style="padding-bottom: 10px;">${notice.content }</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="right">
		<div class="p-box">
			<div class="bd">
				<div class="chart">
					<div id="chart_gt" style="height: 197px;"></div>
				</div>
			</div>
		</div>
		<div class="p-box right-box">
			<div id="preview_title" class="hd">
				<ul>
					<li>经安全检查（巡视）以下五项不安全因素必须引起重视</li>
					<li>经安全检查（巡视）近期高频率不安全因素请关注</li>
				</ul>
			</div>
			<div class="bd" style="height: 310px;">
				<div id="preview_reason">
					<ul>
						<li class="cause">
						</li>
						<li>
							<c:forEach items="${oldCause}" var="oldCause" varStatus="number">
								<div class="info">
									${number.index+1 }.${oldCause.contactName}：${oldCause.attributeName}。<em class="fuchsia">应对提示：${oldCause.attributeMeasures}</em>
								</div>
							</c:forEach>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	
	/**
	 * 获取服务器端数据变量
	 */
	var serverTime = "${serviceTime}";
	// 生产开始日期
	var begintime = "${config.begin_time}";
	// 生产结束日期
	var endtime = "${config.end_time}";
	// 每天的生产动态指标
	var targetPoint = [];
	var POINTS;
	// 折线图显示工期天数,0代表全部显示
	var showDays = "${config.chart_day}";
	setTimeout(function() {
		onDeviceReady();
	}, 500);
	// -----------------------------------------------------------------
	function onDeviceReady() {
		var todayPoint = $("#load_content").data("todayPoint"); //今天触点信息的分值
		$.get("safeLog/score", function(data) { 
			if (data.status == 1) {
				var point = data.data.targetPoint;
				for (var i = 0; i < point.length; i++) {
					targetPoint.push(point[i].point);
				}
				targetPoint.push(parseInt(todayPoint));//加入分值
				appInit();
            } else {
                $.alert(data.info);
            }
		}, "json")
	}
	function appInit() {
		getServerTime();
		countTime();
		var targetLevel;
		var targetPointLen = targetPoint.length -1;
		if (targetPoint[targetPointLen] > targetPoint[targetPointLen -1]) {
			targetLevel = ',高于昨天';
		} else if (targetPoint[targetPointLen] == targetPoint[targetPointLen -1]) {
			targetLevel = ',等于昨天';
		} else if (targetPoint[targetPointLen] < targetPoint[targetPointLen -1]) {
			targetLevel = ',低于昨天';
		}
		$("#target_point").text(targetPoint[targetPointLen]);
		POINTS = targetPoint[targetPointLen];
		chartLoad();
	}	
	// 计算时间
	function countTime() {
		var begintime_ms = Date.parse(new Date(begintime.replace(/-/g, "/")));
		var endtime_ms = Date.parse(new Date(endtime.replace(/-/g, "/")));
		var theDay = serverTime*1000  - begintime_ms;
		var days = Math.floor((endtime_ms - begintime_ms)/(24*3600*1000) + 1);
		// 第theDay天
		var theDayFmt = Math.floor(theDay/(24*3600*1000) + 1);
		// 倒计时
		var countDown = days - theDayFmt;
		if (showDays == 0) {
			showDays = days;
		}
		$("#the_day").text(theDayFmt);
		$("#count_down").text(countDown);
	}
	// 图表
	function chartLoad() {
		// 对应天数的动态指标数据
		var targetPointNow = targetPoint.slice(-showDays);
		// 对应天数
		var dayArray = [];
		if (showDays < 11) {
			for (var i = 1; i <= targetPoint.length; i++) {
				dayArray.push('第'+i+'天');
			}
		} else {
			for (var i = 1; i <= targetPoint.length; i++) {
				dayArray.push(i);
			}
		}
		dayArray = dayArray.slice(-showDays);
	    $('#chart_gt').highcharts({
	    	colors: ['#fff'],
	    	chart: {
	    		backgroundColor: '#f7f7f7'
	    	},
	    	title: {
	    		useHTML: true,
	            text: "今天安全生产动态指数为<span class='orange chart-point'>"+POINTS+"</span>分",
	            style: {
                	fontSize: "18px"
                },
	            x: -20
	        },
	        exporting: {
	        	enabled: false
	        },
	        xAxis: {
	            categories: dayArray,
	            labels: {
	            	style: {
	                	fontSize: "12px"
	                }
	            }
	        },
	        yAxis: {
	        	title: {
		            enabled: false
		        },
	            labels: {
	            	style: {
	                	fontSize: "12px"
	                }
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }],
	            plotBands: [{
	                color: '#ff4136',
	                from: 0,
	                to: 60
	            },{
	                color: '#ffdc00',
	                from: 60,
	                to: 70
	            },{
	                color: '#37b243',
	                from: 70,
	                to: 100
	            }],
	            min: 50,
	            max: 100,
	            gridLineWidth: 0,
	            tickPositions: [50, 60, 80, 100]
	        },
	        tooltip: {
	            valueSuffix: '分'
	        },
	        plotOptions: {
	            line: {
	            	lineWidth: 2,
	                dataLabels: {
	                    enabled: true,
	                    style: {
	                    	fontSize: "12px",
	                    	color: '#fff'
	                    }
	                },
	                enableMouseTracking: false
	            }
	        },
	        series: [{
	            name: '安全生产动态指标',
	            data: targetPointNow
	        }],
	        credits: {
	            enabled: false
	        },
	        legend: {
	            enabled: false
	        }
	  
	    });
	}
	// 信息滚动
	/* function scrollMsg() {
		$('#preview_notic').vTicker({
	      speed: 500,
	      pause: 10000,
	      showItems: 2,
	      animation: 'fade',
	      mousePause: false,
	      height: 0,
	      direction: 'down'
	    });
	} */
	//滚动横幅显示
	(function (){
	    var i=0;
	    var _w = $(window).width();
	    var $dom = $("#preview_notic");
	    var height = $dom.height();
	    var time;
	    clearInterval(time);
	    if (height > 110) {
	    	time = setInterval(_scroll,100);
	    } else {
	    	clearInterval(time);
	    }
	    function _scroll(){
	        $dom.css("marginTop",i);
	        i-=1;
	        if(parseInt($dom.css("marginTop")) <= -height){
	            i = 120;
	        }
	    }
	})();
	// 获取服务器时间
	function getServerTime() {
		//计算与服务器的时差
		var localTime = Math.round(new Date().getTime()/1000);
		var timeMissing = serverTime - localTime;
		var Time;
		if (Time) {
			clearTimeout(Time);
		}
		//运行
		serverTimer();

		function serverTimer(){
		     //换算出服务器的实时时间
		     var currentDate = new Date((Math.round(new Date().getTime()/1000)+timeMissing) * 1000);
		     var y = currentDate.getFullYear();
		     var M = fixTime(currentDate.getMonth()+1);
		     var d = fixTime(currentDate.getDate());
		     var h = fixTime(currentDate.getHours());
		     var m = fixTime(currentDate.getMinutes());
		     var s = fixTime(currentDate.getSeconds());
		     function fixTime(i)
		     {
		          if (i<10){
		               i="0" + i;
		          }
		            return i;
		     }
		     //实时显示在页面上
		     $("#get_time").html(y +"-"+ M +"-"+ d +" "+ h+":"+m+":"+s);
		     Time = setTimeout(serverTimer,1000);
		}
	}
</script>