<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1400px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .header {
            background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        .header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        
        .header .subtitle {
            font-size: 1.2em;
            opacity: 0.9;
        }
        
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            padding: 30px;
            background: #f8f9fa;
        }
        
        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
        }
        
        .stat-number {
            font-size: 2.5em;
            font-weight: bold;
            color: #3498db;
        }
        
        .stat-label {
            color: #7f8c8d;
            margin-top: 5px;
        }
        
        .controls {
            padding: 20px 30px;
            background: #ecf0f1;
            border-bottom: 1px solid #bdc3c7;
        }
        
        .search-box {
            width: 100%;
            padding: 12px;
            border: 2px solid #3498db;
            border-radius: 8px;
            font-size: 16px;
            outline: none;
            transition: border-color 0.3s ease;
        }
        
        .search-box:focus {
            border-color: #2980b9;
        }
        
        .filters {
            margin-top: 15px;
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        
        .filter-btn {
            padding: 8px 16px;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .filter-btn.active {
            background: #3498db;
            color: white;
        }
        
        .filter-btn:not(.active) {
            background: #bdc3c7;
            color: #2c3e50;
        }
        
        .log-container {
            max-height: 600px;
            overflow-y: auto;
            padding: 0;
        }
        
        .log-entry {
            padding: 15px 30px;
            border-bottom: 1px solid #ecf0f1;
            transition: background-color 0.3s ease;
            display: grid;
            grid-template-columns: 150px 80px 200px 1fr;
            gap: 15px;
            align-items: center;
        }
        
        .log-entry:hover {
            background-color: #f8f9fa;
        }
        
        .log-entry.log-error {
            background-color: #fff5f5;
            border-left: 4px solid #e74c3c;
        }
        
        .log-entry.log-warn {
            background-color: #fffbf0;
            border-left: 4px solid #f39c12;
        }
        
        .log-entry.log-info {
            background-color: #f0f8ff;
            border-left: 4px solid #3498db;
        }
        
        .log-entry.log-debug {
            background-color: #f8f9fa;
            border-left: 4px solid #95a5a6;
        }
        
        .timestamp {
            font-family: 'Courier New', monospace;
            font-size: 0.9em;
            color: #7f8c8d;
        }
        
        .level {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 0.8em;
            font-weight: bold;
            text-align: center;
        }
        
        .level-error {
            background: #e74c3c;
            color: white;
        }
        
        .level-warn {
            background: #f39c12;
            color: white;
        }
        
        .level-info {
            background: #3498db;
            color: white;
        }
        
        .level-debug {
            background: #95a5a6;
            color: white;
        }
        
        .metric {
            font-size: 0.9em;
            color: #2c3e50;
            font-weight: 500;
        }
        
        .message {
            font-size: 0.95em;
            line-height: 1.4;
            color: #34495e;
            font-family: 'Courier New', monospace;
        }
        
        .footer {
            padding: 20px 30px;
            background: #2c3e50;
            color: white;
            text-align: center;
        }
        
        @media (max-width: 768px) {
            .log-entry {
                grid-template-columns: 1fr;
                gap: 5px;
            }
            
            .header h1 {
                font-size: 2em;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>⚡ ${title}</h1>
            <div class="subtitle">Generated on ${timestamp}</div>
        </div>
        
        <div class="stats">
            <div class="stat-card">
                <div class="stat-number">${totalEntries}</div>
                <div class="stat-label">Performance Metrics</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${reportType}</div>
                <div class="stat-label">Report Type</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">📊</div>
                <div class="stat-label">Interactive Report</div>
            </div>
        </div>
        
        <div class="controls">
            <input type="text" class="search-box" id="searchBox" placeholder="Search in performance logs...">
            <div class="filters">
                <button class="filter-btn active" data-level="all">All</button>
                <button class="filter-btn" data-level="error">Error</button>
                <button class="filter-btn" data-level="warn">Warning</button>
                <button class="filter-btn" data-level="info">Info</button>
                <button class="filter-btn" data-level="debug">Debug</button>
            </div>
        </div>
        
        <div class="log-container" id="logContainer">
            <#list logEntries as entry>
                <div class="log-entry ${entry.levelClass}" data-level="${entry.level?lower_case}">
                    <div class="timestamp">${entry.timestamp}</div>
                    <div class="level level-${entry.level?lower_case}">${entry.level}</div>
                    <div class="metric">${entry.logger}</div>
                    <div class="message">${entry.formattedMessage}</div>
                </div>
            </#list>
        </div>
        
        <div class="footer">
            <p>TestAutomation_with_DevTools - Performance Report</p>
        </div>
    </div>
    
    <script>
        // Search functionality
        const searchBox = document.getElementById('searchBox');
        const logContainer = document.getElementById('logContainer');
        const filterBtns = document.querySelectorAll('.filter-btn');
        
        searchBox.addEventListener('input', filterLogs);
        
        filterBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                filterBtns.forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                filterLogs();
            });
        });
        
        function filterLogs() {
            const searchTerm = searchBox.value.toLowerCase();
            const activeFilter = document.querySelector('.filter-btn.active').dataset.level;
            const logEntries = logContainer.querySelectorAll('.log-entry');
            
            logEntries.forEach(entry => {
                const message = entry.querySelector('.message').textContent.toLowerCase();
                const level = entry.dataset.level;
                const matchesSearch = message.includes(searchTerm);
                const matchesFilter = activeFilter === 'all' || level === activeFilter;
                
                if (matchesSearch && matchesFilter) {
                    entry.style.display = 'grid';
                } else {
                    entry.style.display = 'none';
                }
            });
        }
        
        // Auto-scroll to bottom
        logContainer.scrollTop = logContainer.scrollHeight;
    </script>
</body>
</html>
