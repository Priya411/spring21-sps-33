// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

var firebaseConfig = {
      apiKey: "AIzaSyA9jmDNzrZXS8XP0zBzOgSKB4_M-Ae5TT8",
      authDomain: "spring21-sps-33.firebaseapp.com",
      databaseURL: "https://spring21-sps-33-default-rtdb.firebaseio.com",
      projectId: "spring21-sps-33",
      storageBucket: "spring21-sps-33.appspot.com",
      messagingSenderId: "398000493644",
      appId: "1:398000493644:web:8c0086673917fd897f90e9"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

var db = firebase.firestore();

// Use the DefaultTableT50Servlet to populate data by default
async function loadData(){
    const response = await fetch('/default', {method:'GET'});
    const response_text = await response.text();
    document.getElementById("contribsTable").innerHTML = response_text;
}

// Find total contributions for a candidate for a specific year
async function loadContributions(id, year){
    var docRef = db.collection("fec_data").doc(id);
    const doc = await docRef.get();
    try {
        if (doc.exists) {
            var candidate = doc.data();
            con = candidate[year].totalContributions;

            if (con!=null){
                return con;
            } else {
                return null;
            }
        } else {
            return null; 
        }
    } catch(error){
        console.log(error)
    }
}

// Read data from the API and dynamically populate table 
async function fecQuery() {
    let query = document.getElementById('query').value; 

    const params = new URLSearchParams({
        query: query
    });

    if (query!=""){
        const response = await fetch('/fec?'+ params.toString(), {method:'GET'});
        const response_json = await response.json();
        console.log(response_json);
        localStorage.setItem('fec_query_response',response_json);

        var htmlString = "<tr><th>Name</th><th>Political Party</th><th>Total Contributions <p style=\"visibility:hidden;\" id=\"currentYear\">hello</p><form><select id =\"yearForm\" onchange=\"fecQuery()\"><option>---Choose a year---</option><option>2001-2002</option><option>2003-2004</option><option>2005-2006</option><option>2007-2008</option><option>2009-2010</option><option>2011-2012</option><option>2013-2014</option><option>2015-2016</option><option>2017-2018</option><option>2019-2020</option><option>2021-2022</option></form></th></tr>";

        var yearForm = document.getElementById("yearForm");
        var year; 

        if (yearForm!=null){
            year = yearForm.options[yearForm.selectedIndex].text;
            if (year=="---Choose a year---"){
                year = null;
            } 
        }
        
        for (entry of response_json) {
            candidate = entry['results'][0];
            name = candidate['name'];
            party = candidate['party'];
            id = candidate['candidate_id'];

            if (year!=null){
                totalCon = await loadContributions(id, year); 
                
                if (totalCon!=null){
                    htmlString += `<tr><td>${name}</td><td>${party}</td><td>${totalCon}</td></tr>`;
                } else {
                    htmlString += `<tr><td>${name}</td><td>${party}</td><td>---</td></tr>`;
                }
            } else {
                htmlString += `<tr><td>${name}</td><td>${party}</td><td>---</td></tr>`;
            }
        }
        document.getElementById("contribsTable").innerHTML = htmlString;
        document.getElementById("currentYear").style.visibility = "visible"; 
        document.getElementById("currentYear").innerHTML = "(" + year + ")"; 
    } else {
        loadData();
    }
}

// Google Charts Library @ https://developers.google.com/chart 
// ---------------------------------------------------------------
google.charts.load('current', {'packages':['corechart', 'line']});
google.charts.setOnLoadCallback(drawCandidateLineChart);
google.charts.setOnLoadCallback(drawCandidatePieChart);
google.charts.setOnLoadCallback(drawDynamicLineChart);

// Line Chart: plot of total contributions per year for a candidate
function drawCandidateLineChart() {
    /*  // Make servlet call here to get array
        // modeled after this:
        const params = new URLSearchParams({
        query: document.getElementById('query').value
        });
        const response = await fetch('/candidatecontributionhistory?'+ params.toString(), {method:'GET'});
        //localStorage.setItem('candidatecontributionhistory_response',response_json);

        log(response);
        // returns a json -> log to debug, extract array */

    // const data = new google.visualization.arrayToDataTable(array);       // Use this line instead of those below if we have an array as input
    const data = new google.visualization.arrayToDataTable([
          ['Year', '$'],
          ['1988',  3980894],
          ['2008',  14272654],
          ['2020',  1074179976],
        ]);
  
    const options = {
        'title': 'Total Contributions Per Election',
        'legend': {position:'none'},
        'vAxis.logScale': 'true',
        'vAxis.scaleType': 'log',   // Can only make log scales from continuous axis values
        'backgroundColor': '#63687E', 
        'colors':['#C3D350'],  
        'width':1000,
        'height':500,
        //'tooltip.textStyle' : {'fontSize': 14},   // This could fix the tooltip text spilling out of its box, but is overridden by #linechart text in style.css which is necessary to make all the chart text black
        //'margin-left': auto,                      // Meant to center the chart, but not working.
        //'margin-right': auto
    };
    
    const chart = new google.visualization.LineChart(document.getElementById('linechart'));
    chart.draw(data, options);
}

// Pie Chart: Categories of funding sources for a candidate
function drawCandidatePieChart() {

    //const data = new google.visualization.arrayToDataTable(array);    // Use this line instead of those below if we have an array as input
    const data = new google.visualization.arrayToDataTable([
          ['Source', '$'],
          ['Individual Contributions',  823669348],
          ['Party Committee',  1170],
          ['Other Committee',  243411324],
          ['Offsets to Expenditures',  7021943],
          ['Other Receipts', 77361]
        ]);

    const options = {
        'title': 'Categories of Funding Sources for 2020',
        'legend': {position:'none'},
        'backgroundColor': '#63687E', 
        //'colors':['#94C9A9', '#51567B', '#885053', '#FFFFFF'],  // Customize the colors of the pie chart sections 
        'width':800,
        'height':400
    };
    
    const chart = new google.visualization.PieChart(document.getElementById('piechart'));
    chart.draw(data, options);
}

// Line Chart: plot of total contributions per year for a candidate (non-hardcoded data)
function drawDynamicLineChart() {
    
    //var array = // Get array from CandidatePageServlet (see servlet line 46 results.toString())
    const data = new google.visualization.arrayToDataTable(array);
  
    const options = {
        'title': 'Total Contributions Per Election',
        'legend': {position:'none'},
        'vAxis.logScale': 'true',
        'vAxis.scaleType': 'log',   // Can only make log scales from continuous axis values
        'backgroundColor': '#63687E', 
        'colors':['#C3D350'],  
        'width':1000,
        'height':500,
        //'tooltip.textStyle' : {'fontSize': 14},   // This could fix the tooltip text spilling out of its box, but is overridden by #linechart text in style.css which is necessary to make all the chart text black
        //'margin-left': auto,                      // Meant to center the chart, but not working.
        //'margin-right': auto
    };
    
    const chart = new google.visualization.LineChart(document.getElementById('dynamicLineChart'));
    chart.draw(data, options);
}