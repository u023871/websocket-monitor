	var stompClient = null;
	var dynCpuStatsWritten = false;
	var staticSystemStatsWritten = false;
	var currentHost = 0;
	
	var ip = null;

	
	/**
	 * init on load
	 */
	function initialize() {
		
		disconnect();
		loadHosts();
	}
	
	/**
	 * load hosts on load
	 */
	function loadHosts() {
		
	    $.ajax({
	        url: "http://localhost:9090/monitor/hosts"
	    }).then(function(data) {
	    	
			if(data != null) {
				
				var hosts = data._embedded.hosts;
				
				for (var i = 0; i < hosts.length; i++){
					
					writeTableRow(hosts[i], i, "", "hostsRow", "hostsTable", false, "host-");
				}

				// ensure to remove template row
				var hostsRowNode = document.getElementById("hostsRow");
				if(hostsRowNode != null) {
					
					hostsRowNode.outerHTML = "";
				}
			}

		    setCurrentHost(hosts[0]);
		});
	};
	
	/**
	 * load hosts on load
	 */
	function getHost(id) {
		
	    var host;

	    $.ajax({
	        type: "GET",
	        url: "http://localhost:9090/monitor/hosts/" + id,
	        async: false,
	        success : function(data) {
	            host = data;
	        }
	    });

	    return host;
	};
	
	/**
	 * load hosts on load
	 */
	function setCurrentHost(host) {
		
		if(host != null) {
			
	    	currentHost = host;
	    	
		   	$('#currentHost').text(host.name);
		}
	};
	
	/**
	 * set status to dis/connected
	 */
	function setConnected(connected) {
		
		document.getElementById('connect').disabled = connected;
		document.getElementById('disconnect').disabled = !connected;
		
		if(connected) {
			
			document.getElementById('disconnect').classList.remove('disabled');
			document.getElementById('connect').classList.add('disabled');
		} else {
			document.getElementById('disconnect').classList.add('disabled');
			document.getElementById('connect').classList.remove('disabled');
		}
	}
	
	function connect() {
		connectHost(1);
	}

	/**
	 * connect Stomp client and add subscriptions
	 */
	function connectHost(hostId) {
		
		// clear old connection
		disconnect();
		
		var host = getHost(hostId);

		if(host != null) {
			
			setCurrentHost(host);
			
 			// var socket = new SockJS('/hello');
			var socket = new SockJS('http://192.168.1.43:9090/monitor/hello');
			stompClient = Stomp.over(socket);
			
			/* enable Stomp client debugging by uncommenting
			// append the debug log to a #debug div somewhere in the page using JQuery:
			stompClient.debug = function(str) {

				$("#debug").append(str + "\n");
			};
			*/
			
			stompClient.connect({}, function(frame) {
				
				setConnected(true);
				console.log('Connected: ' + frame);
				
				// subscribe static system status broker
				stompClient.subscribe('/status/system/static/' + host.ip, function(staticSystemStatus) {
					
					var cpuStatsStatic = JSON.parse(staticSystemStatus.body).staticCpuInfo;
					showStaticCpuStats(cpuStatsStatic);
					
					var sysStatsStatic = JSON.parse(staticSystemStatus.body).staticSystemInfo;
					showStaticSystemStats(sysStatsStatic);
					
					var fsStatsStatic = JSON.parse(staticSystemStatus.body).staticFileSystemInfos;
					showStaticFileSystemStats(fsStatsStatic);
					
					staticSystemStatsWritten = true;
				});
				
				// subscribe dynamic system status broker
				stompClient.subscribe('/status/system/dynamic/' + host.ip, function(dynamicSystemStatus) {
					
					var cpuPercCombined = JSON.parse(dynamicSystemStatus.body).cpuPercCombined;
					var memPercUsed = JSON.parse(dynamicSystemStatus.body).memPercUsed;
					var uptime = JSON.parse(dynamicSystemStatus.body).uptime;
					
					updateLight(cpuPercCombined, 'cpuPercCombined');
					updateLight(memPercUsed, 'memPercUsed');
					
					updateValue(uptime, 'uptime');
					
					var cpuStatsDyn = JSON.parse(dynamicSystemStatus.body).dynamicCpuInfos;
					showDynamicCpuStats(cpuStatsDyn);
					
					dynCpuStatsWritten = true;
				});
			});
			
			
		}
	}

	/**
	 * disconned Stomp client
	 */
	function disconnect() {
		if (stompClient != null) {
			stompClient.disconnect();
		}
		setConnected(false);
		console.log("Disconnected");
	}

	/**
	 * show static CPU stats
	 */
	function showStaticCpuStats(cpuStats) {
		
		if(cpuStats.cpuCacheSize > 0) {
			
			replaceValue(cpuStats.cpuCacheSize, 'cpuCacheSize');
		}
		
		replaceValue(cpuStats.cpuTotalCores, 'cpuTotalCores');
		replaceValue(cpuStats.cpuMhz, 'cpuMhz');
		replaceValue(cpuStats.cpuModel, 'cpuModel');
		replaceValue(cpuStats.cpuVendor, 'cpuVendor');
	}
	
	/**
	 * show other static stuff
	 */
	function showStaticSystemStats(sysStats) {
		
		if(sysStats != null) {
			
			// FQDN
			replaceValue(sysStats.fqdn, 'fqdn');
		}
	}
	
	
	/**
	 * show static file system stuff
	 */
	function showStaticFileSystemStats(fsStats) {
		
		// static file system info
		if(fsStats != null) {
			
			for (var i = 0; i < fsStats.length; i++){
				
				writeTableRow(fsStats[i], i, "", "staticFileSystemRow", "staticFileSystemTable", staticSystemStatsWritten, "");
			}

			// ensure to remove template row
			var staticFileSystemRowNode = document.getElementById("staticFileSystemRow");
			if(staticFileSystemRowNode != null) {
				
				staticFileSystemRowNode.outerHTML = "";
			}
		}
	}
	
	/**
	 * show dynamic CPU stats
	 */
	function showDynamicCpuStats(cpuStats) {
		
		if(cpuStats != null) {
			
			//
			// CPU util. percentages
			//
			
			for (var i = 0; i < cpuStats.length; i++){
				
				writeTableRow(cpuStats[i], i, 'Perc', 'dynamicCpuRow', 'dynamicCpuTable', dynCpuStatsWritten, "");
			}

			// ensure to remove template row
			var dynamicCpuRowNodeOrig = document.getElementById('dynamicCpuRowPerc');
			if(dynamicCpuRowNodeOrig != null) {
				
				dynamicCpuRowNodeOrig.outerHTML = "";
			}

			//
			// CPU util. times
			//
			
			for (var i = 0; i < cpuStats.length; i++){
				
				writeTableRow(cpuStats[i], i, 'Times', 'dynamicCpuRow', 'dynamicCpuTable', dynCpuStatsWritten, "");
			}
			
			// ensure to remove template row
			dynamicCpuRowNodeOrig = document.getElementById('dynamicCpuRowTimes');
			if(dynamicCpuRowNodeOrig != null) {
				
				dynamicCpuRowNodeOrig.outerHTML = "";
			}
		}

	}
	
	/**
	 * cpu: dyn. CPU json data object
     * index: CPU index 
	 * suffix: dynamicCpuRow id suffix, e.g. Perc
	 */
	function writeTableRow(rowBean, index, suffix, rowElementName, tableElementName, rowTemplateCloneAlreadyAppendedAndUnhidden, fieldPrefix) {
		
		 
		var rowNodeOrig = document.getElementById(rowElementName + suffix);
		var rowNode = rowNodeOrig;

		var rowNodeClone = null;

		if(rowNode == null) {
			
			$jqClone = $('#' + rowElementName + suffix + index).clone();
			
		} else {
			
			$jqClone = $('#' + rowElementName + suffix).clone();

		}

		// this works only if there is exactly one match
		if($jqClone != null && $jqClone.length > 0) {
			
			rowNodeClone = $jqClone.get()[0];
		}
		
		var tableNode = document.getElementById(tableElementName + suffix);
		
		var divTags = rowNodeClone.getElementsByTagName('td');

		// iterate all div tags and append index to them
		for(var i=0; i<divTags.length; i++) {
			
			var divElement = divTags[i];
			
			var id = divElement.id;
			
			if(id != null && '' != id) {
				
				divElement.id = id + index;
			}
		}
		
		if(rowNodeClone != null) {
			
			// set new id to clone
			rowNodeClone.id = rowElementName + suffix + index;
		
			// unhide and append clone if row has not yet been rendered before
			if(!rowTemplateCloneAlreadyAppendedAndUnhidden) {
				
			    tableNode.appendChild(rowNodeClone);
			    rowNodeClone.classList.remove('hidden');
			}
		}

		// replace all elements' values matching bean property names
	    for (var key in rowBean) {
	    	
	        var elementName = fieldPrefix + key;
	        var value = rowBean[key];
	        
	        if(elementName.lastIndexOf('Perc') > -1) {
	        	
	        	value = $.format.number(value * 100, '##0.00') + '%';
	        }
	        else if(elementName.lastIndexOf('Time') > -1) {
	        	
	        	value = $.format.number(value / 1000, '##0.00') + 's';
	        }
	        else if(elementName.lastIndexOf('cpuNumber') > -1) {
	        	
				// as we reuse the "cpuNumber" element in different tables, add a suffix
	        	elementName = elementName + suffix;
	        }
	        
	        replaceValue(value, elementName + index);
	        
	    }
		
	}
	
	/**
	 * replace a value (as text node) into a node
	 */
	function replaceValue(value, nodeName) {
		
		var node = document.getElementById(nodeName);

		if(node != null) {
			
// 			node.innerHTML = value;
//  			delete node.value;
// 			node.value = value;
			node.replaceChild(document.createTextNode(value), node.childNodes[0]);
		}
	}

	/**
	 * update some element with a value but format before depending on content
	 */
	function updateValue(value, elementName) {
		
		var elementStatusBox = document.getElementById(elementName);

		// uptime
		if (elementName == 'uptime') {
			
			if(value != null && value > 0) {
				
			} else {
				
				value = 'NO DATA';
			}
			
			if(typeof value == 'number') {
				
				value = $.format.number(value, '##0.00');
			}

		}

		if (value != null) {

			// debug only
			var elementDebug = document.getElementById('debug');
			var emptyTextNode = document.createTextNode('');
			elementDebug.replaceChild(emptyTextNode, elementDebug.childNodes[0]);
			elementDebug.classList.remove('hidden');

			// make visible
			elementStatusBox.classList.remove('hidden');
		}
		
		replaceValue(value, elementName);
	}

	/**
	 * update major system status lights into red/green/yellow
	 */
	function updateLight(value, elementNamePrefix) {


		// $("#debug").append("content: " + content + "\n");

		var elementLight = document.getElementById(elementNamePrefix + 'Light');
		var elementStatusBox = document.getElementById(elementNamePrefix);

		if (value != null) {

			// debug only
			var elementDebug = document.getElementById('debug');
			var emptyTextNode = document.createTextNode('');
			elementDebug.replaceChild(emptyTextNode, elementDebug.childNodes[0]);
			elementDebug.classList.remove('hidden');

			// make visible
			elementStatusBox.classList.remove('hidden');
		}

		elementStatusBox.classList.remove('lightRed');
		elementStatusBox.classList.remove('lightGreen');
		elementStatusBox.classList.remove('lightYellow');
		elementStatusBox.classList.remove('lightInactive');

		
		if (value > 66) {
			
			elementStatusBox.classList.add('lightRed');
		} else if (value <= 66 && value > 33) {
			
			elementStatusBox.classList.add('lightYellow');
		} else if (value <= 33 && value >= 0) {
			
			elementStatusBox.classList.add('lightGreen');
		} else {
			
			elementStatusBox.classList.add('lightInactive');
		}

		if(typeof value == 'number') {
			
			value = $.format.number(value, '##0.00');
		}

		var newValueTextNode = document.createTextNode(value);

		elementLight.replaceChild(newValueTextNode, elementLight.childNodes[0]);
	}

	/**
	 * replace a string inside another string 
	 */
	function replaceAll(str, find, replace) {

		return str.replace(new RegExp(escapeRegExp(find), 'g'), replace);
	}

	/**
	 * escape regex patten in a string
	 */
	function escapeRegExp(str) {
		return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
	}
