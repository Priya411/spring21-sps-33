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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

// Google Charts Library @ https://developers.google.com/chart 
// ---------------------------------------------------------------
google.charts.load('current', {'packages':['corechart', 'line']});
google.charts.setOnLoadCallback(drawChart);

// Create a line chart and add it to the page.
function drawChart() {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Year');
    data.addColumn('number', '%');
        data.addRows([
            ['1999', 58],
            ['2000', 62],
	        ['2001', 62],
	        ['2002', 60],
	        ['2003', 60],
	        ['2004', 61],
            ['2005', 62],
            ['2006', 61],
            ['2007', 65],
            ['2008', 62],
            ['2009', 57],
            ['2010', 56],
            ['2011', 54],
            ['2012', 53],
            ['2013', 52],
            ['2014', 54],
            ['2015', 55],
            ['2016', 52],
            ['2017', 54],
            ['2018', 55],
            ['2019', 55],
            ['2020', 55],
        ]);

    const options = {
        'title': 'Percentage of American adults investing in the stock market from 1999 - 2020',
        'legend': {position:'none'},
        'backgroundColor': 'WhiteSmoke', 
        'colors':['#84a98c'],  
        'width':801,
        'height':400
    };
    
    const chart = new google.visualization.LineChart(document.getElementById('chart-container'));
    chart.draw(data, options);
}