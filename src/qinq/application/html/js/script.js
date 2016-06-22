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

function createPlayer() {
  var name = document.getElementById('name-field').value;
  //TODO send name to server, get id
  if(send_success) {
    document.getElementById("question-box").style.display = 'none';
    player_id = -1;//TODO
  }
  else {
    //Try again?
  }
}

function submitAnswer() {
  var answer = document.getElementById('answer-field').value;
  document.getElementById("question-box").style.display = 'none';
  //TODO send answer and player id to server
  //document.getElementById(element_id).style.display = 'block'; //show element
  var xmlhttp;
  if (window.XMLHttpRequest) {
      xmlhttp = new XMLHttpRequest(); //for IE7+, Firefox, Chrome, Opera, Safari
  }
  else {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); //for IE6, IE5
  }
  
  //aid = document.getElementById('answer-field').aid
  
  var data = {'action':'send answer', 'id':aid, 'answer':answer}
  
  //Create a asynchronous GET request
  xmlhttp.open("GET", 'data.json', true);
  xmlhttp.setRequestHeader("ajax", "true");
  xmlhttp.setRequestHeader("Content-type", "application/json");
  xmlhttp.send(JSON.stringify(data));
   
  //When readyState is 4 then get the server output
  xmlhttp.onreadystatechange = function() {
    if (xmlhttp.readyState == 4) { 
      if (xmlhttp.status >= 200 && xmlhttp.status <= 299) {
        //DONE
      } 
      else {
        alert('Something is wrong !!');
      }
    }
  };
}

function submitVote() {
  //TODO
}

function getQuestion(question, question_id) {
  x.op
  //TODO set question
  //TODO make visible
  //TODO add timer
  //TODO
  
}

function getVoteChoices() {
  //TODO
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