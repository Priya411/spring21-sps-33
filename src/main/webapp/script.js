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

// Reading data from database and writing it to the page
db.collection("fec_data").where("2019-2020.affiliation", "==", "REP")
    //.withConverter(dataConverter)
    .get()
    .then((querySnapshot) => {
        querySnapshot.forEach((doc) => {
            // doc.data() is never undefined for query doc snapshots
            var candidate = doc.data();
            document.getElementById("candidate").innerHTML = doc.id;
            document.getElementById("party").innerHTML = candidate["2019-2020"].affiliation;
            document.getElementById("state").innerHTML = candidate["2019-2020"].state;
            document.getElementById("conFromPoliticalComm").innerHTML = candidate["2019-2020"].conFromPoliticalComm;
            document.getElementById("conFromPartyComm").innerHTML = candidate["2019-2020"].conFromPartyComm;
        });
    })
    .catch((error) => {
        console.log("Error getting documents: ", error);
    });