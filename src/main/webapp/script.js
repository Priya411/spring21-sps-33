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
    var header = "<tr><th>Name</th><th>Political Party</th><th>Total Contributions <br>(2019-2020)</th></tr>";
    var table_content = header + response_text; 
    document.getElementById("contribsTable").innerHTML = table_content;
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
        var htmlString = "<tr><th>Name</th><th>Political Party</th><th>Office</th></tr>";
        for (entry of response_json) {
            candidate = entry['results'][0];
            name = candidate['name'];
            party = candidate['party'];
            office = candidate['office_full'];
            htmlString += `<tr><td>${name}</td><td>${party}</td><td>${office}</td></tr>`;
        }
        document.getElementById("contribsTable").innerHTML = htmlString;
    } else {
        loadData();
    }
}

