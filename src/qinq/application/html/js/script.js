/*
 * Copyright (c) 2016, Andriy Zasypkin.
 *
 * This file is part of Qinq.
 *
 * Qinq(or QINQ) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Qinq in distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Qinq. If not, see <http://www.gnu.org/licenses/>.
 */

var player_id = -1;
var timer     = 0;
var aid       = 0;

function onLoad() {
  //visible:
  //document.getElementById("id-name").style.display = 'block';
  
  //not:
  //document.getElementById("id-name").style.display = 'none';
  
  
  document.getElementById("welcome").style.display = 'block';
  document.getElementById("question-box").style.display = 'none';
  document.getElementById("vote-box").style.display = 'none';
  document.getElementById("name-error").style.display = 'none';
  getInfo();
}

function createPlayer() {
  var name = document.getElementById('name-field').value;
  var xmlhttp;
  if (window.XMLHttpRequest) {
      xmlhttp = new XMLHttpRequest(); //for IE7+, Firefox, Chrome, Opera, Safari
  }
  else {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); //for IE6, IE5
  }
  
  //aid = document.getElementById('answer-field').aid
  
  var data = {'action':'create user', 'name':name}
  
  //Create a asynchronous GET request
  xmlhttp.open("POST", 'data.json?q'+Math.random(), true);
  xmlhttp.setRequestHeader("Content-type", "application/json");
  xmlhttp.send(JSON.stringify(data));
   
  xmlhttp.onreadystatechange = function() {
    if (xmlhttp.readyState == 4) { 
      if (xmlhttp.status >= 200 && xmlhttp.status <= 299) {
        var json = JSON.parse(xmlhttp.responseText);
        document.getElementById("question-box").style.display = 'none';
        if(json['created'] == 'true') {
          player_id = json['id'];
          document.getElementById("welcome").style.display = 'none';
        }
        else {
          document.getElementById("welcome").style.display = 'block';
          document.getElementById("name-error").style.display = 'block';
        }
      } 
    }
  };
}

function submitAnswer() {
  document.getElementById("question-box").style.display = 'none';
  document.getElementById("welcome").style.display      = 'none';
  document.getElementById("vote-box").style.display     = 'none';
  var xmlhttp;
  if (window.XMLHttpRequest) {
      xmlhttp = new XMLHttpRequest(); //for IE7+, Firefox, Chrome, Opera, Safari
  }
  else {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); //for IE6, IE5
  }
  
  //TODO get answer from field
  var answer = document.getElementById('answer-field').value;
  //aid = document.getElementById('answer-field').aid
  
  var data = {'action':'send answer', 'id':aid.toString(), 'answer':answer}
  
  //Create a asynchronous GET request
  xmlhttp.open("POST", 'data.json?q='+Math.random(), true);
  xmlhttp.setRequestHeader("Content-type", "application/json");
  xmlhttp.send(JSON.stringify(data));
   
  xmlhttp.onreadystatechange = function() {
    if (xmlhttp.readyState == 4) {
      if (xmlhttp.status >= 200 && xmlhttp.status <= 299) {
        var json = JSON.parse(xmlhttp.responseText);
      } 
    }
  };
}

function submitVote() {
  //TODO
}

function getQuestion(question, question_id) {
  //TODO set question
  //TODO make visible
  //TODO add timer
  //TODO
  
}

function getVoteChoices() {
  //TODO
}


function getInfo() {
  //TODO
  
  window.setTimeout(getInfo(), 1000);
}

function escapeXml(unsafe) {
  return unsafe.replace(/[<>&'"]/g, function (c) {
    switch (c) {
      case '<': return '&lt;';
      case '>': return '&gt;';
      case '&': return '&amp;';
      case '\'': return '&apos;';
      case '"': return '&quot;';
    }
  });
}