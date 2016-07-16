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

var player_id   = -1;
var player_name = '';
var timer       = 0;
var state       = 'waiting';

function onLoad() {
  //visible:
  //document.getElementById("id-name").style.display = 'block';

  //not:
  //document.getElementById("id-name").style.display = 'none';

  if (!String.prototype.trim) {
    String.prototype.trim = function () {
      return this.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');
    };
  }

  document.getElementById("welcome").style.display      = 'block';
  document.getElementById("timer").style.display        = 'none';
  document.getElementById("question-box").style.display = 'none';
  document.getElementById("vote-box").style.display     = 'none';
  document.getElementById("name-error").style.display   = 'none';

  document.getElementById("name-field").addEventListener("keypress",
      function(event) {
    if (event.keyCode == 13) {
      event.preventDefault();
      createPlayer();
    }
  });

  document.getElementById("answer-field").addEventListener("keypress",
      function(event) {
    if (event.keyCode == 13) {
      event.preventDefault();
      submitAnswer();
    }
  });

  document.getElementById("answer-field").addEventListener("keyup",
      function(event) {
    event.preventDefault();
    if (event.keyCode == 13) {
      submitAnswer();
    }
  });
}

function spectate() {
  //TODO
}

function createPlayer() {
  var name = document.getElementById('name-field').value;

  sendData({'action':'create user', 'name':name.trim()}, function(json) {
    if(json['created'] == 'true') {
      player_id   = json['id'];
      player_name = json['name'];
      document.getElementById('name').innerHTML        = player_name;
      document.getElementById("welcome").style.display = 'none';
      document.getElementById("header").style.backgroundColor=json['color'];
      getInfo();
    }
    else {
      document.getElementById("welcome").style.display    = 'block';
      document.getElementById("name-error").style.display = 'block';
    }

  });
}

function submitAnswer() {
  document.getElementById("welcome").style.display      = 'none';
  document.getElementById("vote-box").style.display     = 'none';

  var answer = document.getElementById('answer-field').value;
  var aid = document.getElementById('answer-field').dataset.id;
  if(aid == undefined || !answer.trim()) {
    return null;
  }
  var data = {'action':'send answer', 'id':aid, 'answer':answer}
  sendData(data, function(x) {});
  document.getElementById("question-box").style.display = 'none';
  document.getElementById("timer").style.display        = 'none';

  document.getElementById('answer-field').value         = '';

  state = 'waiting';
}

function submitVote(aid) {
  sendData({'action':'vote', 'id':player_id.toString(),
            'aid':aid.toString()}, function(json) {
    if(json['left'] == 0 || json['left'] == '0') {
      removeElementsByClass('vote-option');
    }
    else {
      //TODO somehow incorporate json['voted'] into this
      //         (number of times voted on that answer)

    }
  });
  state = 'waiting';
}

function getInfo() {
  sendData({'action':'get state', 'state':state, 'id':player_id.toString()},
      function(json) {
    console.log(JSON.stringify(json))

    timer = json['time'];

    if(timer <= 0) {
      document.getElementById("timer").style.display = 'none';
    }

    document.getElementById("timer").innerHTML = timer;

    if(state == 'answering' || state =='voting') {
      if(timer <= 0) {
        state = 'waiting';
        document.getElementById("answer-field").value         = '';
        document.getElementById("question-box").style.display = 'none';
        document.getElementById("vote-box").style.display     = 'none';
      }
      else {
        document.getElementById("timer").style.display = 'block';
      }
    }
    else {
      if(json['action'] == 'answer') {
        document.getElementById("answer-question").innerHTML = json['question'];
        document.getElementById('answer-field').dataset.id   = json['aid'];
        document.getElementById("question-box").style.display = 'block';
        state = 'answering';
      }
      else if(json['action'] == 'vote') {
        document.getElementById("answers").innerHTML = '';
        document.getElementById("vote-question").innerHTML = json['question'];
        document.getElementById("votes-left-span").innerHTML = json['votes'];
        if(json['votes'] > 1)
          document.getElementById("votes-left-div").style.display = 'block';
        else
          document.getElementById("votes-left-div").style.display = 'none';
        var element = document.getElementById("answers");
        json['answers'].forEach(function(answer) {
          var div = document.createElement("div");
          var node = document.createTextNode(answer['answer']);
          div.dataset.id = answer['aid'];
          div.className = 'vote-option';
          div.setAttribute("onclick", "submitVote(" + answer['aid'] + ")");
          div.appendChild(node);
          element.appendChild(div);
        });
        if(json['votes'] > 0){
          document.getElementById("vote-box").style.display = 'block';
          state = 'voting';
        }
      }
    }

    window.setTimeout(getInfo, 1000);
  });
}

function sendData(data, callback) {
  var xmlhttp;
  if (window.XMLHttpRequest) {
      xmlhttp = new XMLHttpRequest(); //for IE7+, Firefox, Chrome, Opera, Safari
  }
  else {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); //for IE6, IE5
  }

  //Create a asynchronous GET request
  xmlhttp.open("POST", 'data.json?q='+Math.random(), true);
  xmlhttp.setRequestHeader("Content-type", "application/json");
  xmlhttp.send(JSON.stringify(data));

  xmlhttp.onreadystatechange = function() {
    if (xmlhttp.readyState == 4) {
      if (xmlhttp.status >= 200 && xmlhttp.status <= 299) {
        callback(JSON.parse(xmlhttp.responseText));
      }
    }
  };
}

function removeElementsByClass(className){
  var elements = document.getElementsByClassName(className);
  while(elements.length > 0){
    elements[0].parentNode.removeChild(elements[0]);
  }
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