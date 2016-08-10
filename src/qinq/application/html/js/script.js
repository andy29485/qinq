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

var ws = new WebSocket("ws://" + location.host + "/whatever");

ws.onopen = function() {
    alert("Opened!");
    ws.send("Hello Server");
};

ws.onmessage = function (evt) {
    alert("Message: " + evt.data);
};

ws.onclose = function() {
    alert("Closed!");
};

ws.onerror = function(err) {
    alert("Error: " + err);
};

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
  
  document.getElementById('name-field').focus();

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
  var name = '';
  sendData({'action':'create user', 'name':name.trim()}, function(json) {
    if(json['created'] == 'true') {
      player_id   = json['id'];
      player_name = json['name'];
      document.getElementById('name').innerHTML               =esc(player_name);
      document.getElementById("welcome").style.display        = 'none';
      document.getElementById("header").style.backgroundColor = json['color'];
      document.getElementById("score").style.display          = 'none';
      //Deprecated: ajax
      //getInfo();
    }
    else {
      document.getElementById("welcome").style.display    = 'block';
    }

  });
}

function createPlayer() {
  var name = document.getElementById('name-field').value;

  sendData({'action':'create user', 'name':name.trim()}, function(json) {
    if(json['created'] == 'true') {
      player_id   = json['id'];
      player_name = json['name'];
      document.getElementById('name').innerHTML        = esc(player_name);
      document.getElementById("welcome").style.display = 'none';
      document.getElementById("header").style.backgroundColor=json['color'];
      //Deprecated: ajax
      //getInfo();
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
  document.getElementById("welcome").style.display      = 'none';

  document.getElementById('answer-field').value         = '';
  state = 'waiting';
}

function submitVote(aid) {
  sendData({'action':'vote', 'id':player_id.toString(),
            'aid':aid.toString()}, function(json) {
    if(json['left'] == 0 || json['left'] == '0') {
      removeElementsByClass('vote-option');
      state = 'waiting';
    }
    else {
      document.getElementById("vts-"+aid).innerHTML='&nbsp;('+json['voted']+')';
    }
  });
}

function getInfo() {
  sendData({'action':'get state', 'state':state, 'id':player_id.toString()},
      function(json) {
    console.log(JSON.stringify(json))

    if(json['action'] == 'die') {
      setTimeout(alert("Game ended, or maybe you got kicked"), 0);
      location.reload();
    }
    
    timer = json['time'];

    if(timer <= 0) {
      document.getElementById("timer").style.display        = 'none';
      document.getElementById("question-box").style.display = 'none';
      document.getElementById("vote-box").style.display     = 'none';
      document.getElementById("welcome").style.display      = 'none';
      state = 'waiting';
    }
    else {
      document.getElementById("timer").style.display        = 'block';
    }

    document.getElementById("timer").innerHTML = timer;
    document.getElementById("score").innerHTML = json['score'];

    if(state == 'answering' || state =='voting') {
      if(timer <= 0) {
        state = 'waiting';
        document.getElementById("answer-field").value         = '';
        document.getElementById("question-box").style.display = 'none';
        document.getElementById("vote-box").style.display     = 'none';
        document.getElementById("results-box").style.display  = 'none';
      }
      else {
        document.getElementById("timer").style.display = 'block';
      }
    }
    else {
      if(json['action'] == 'answer') {
        document.getElementById("answer-question").innerHTML  =
                                                          esc(json['question']);
        document.getElementById('answer-field').dataset.id    = json['aid'];
        document.getElementById("question-box").style.display = 'block';
        document.getElementById("vote-box").style.display     = 'none';
        document.getElementById("welcome").style.display      = 'none';
        document.getElementById("results-box").style.display  = 'none';
        document.getElementById('answer-field').focus();
        state = 'answering';
      }
      else if(json['action'] == 'vote') {
        document.getElementById("timer").style.display        = 'block';
        document.getElementById("vote-box").style.display     = 'block';
        document.getElementById("question-box").style.display = 'none';
        document.getElementById("welcome").style.display      = 'none';
        document.getElementById("results-box").style.display  = 'none';
        document.getElementById("answers").innerHTML          = '';
        document.getElementById("vote-question").innerHTML    =
                                                          esc(json['question']);
        document.getElementById("votes-left-span").innerHTML  = json['votes'];
        if(json['votes'] > 1)
          document.getElementById("votes-left-div").style.display = 'block';
        else
          document.getElementById("votes-left-div").style.display = 'none';
        var element = document.getElementById("answers");
        json['answers'].forEach(function(answer) {
          var div = document.createElement("div");
          var span = document.createElement("span");
          var node = document.createTextNode(answer['answer']);
          div.dataset.id = answer['aid'];
          span.id = 'vts-'+answer['aid'];
          div.className = 'vote-option';
          div.setAttribute("onclick", "submitVote(" + answer['aid'] + ")");
          div.appendChild(node);
          div.appendChild(span);
          element.appendChild(div);
        });
        if(json['votes'] > 0){
          state = 'voting';
        }
      }
      else {
        document.getElementById("question-box").style.display = 'none';
        document.getElementById("vote-box").style.display     = 'none';
        document.getElementById("results-box").innerHTML = '';
        var element = document.getElementById("results-box");
        
        if(json['info'] != 'none' && json['info']['info'] != 'none') {
          if(json['info']['info'] == 'answering') {

            var node = document.createTextNode('WAITING FOR PLAYERS:');
            var info_div = document.createElement("div");
            info_div.className = 'message';
            info_div.appendChild(node);

            element.appendChild(info_div);
            
          }
          if(json['info']['info'] == 'answering'
           || json['info']['info'] == 'round') {
            var div = document.createElement("div");
            div.id  = 'info';
            json['info']['players'].forEach(function(player) {
              var player_div = document.createElement("div");
              var node = document.createTextNode(player['name']);
              player_div.className = 'player';
              player_div.style.backgroundColor = player['color'];
              player_div.appendChild(node);
              div.appendChild(player_div);
            });
            element.appendChild(div);
          } 
          else if(json['info']['info'] == 'question') {

            var div = document.createElement("div");
            div.id = 'answers';
            
            json['info']['answers'].forEach(function(answer) {
              var answer_div_wrap = document.createElement("div");
              answer_div_wrap.className = 'result-answer-wrapper';
              
              var answer_div = document.createElement("div");
              answer_div.className = 'result-answer';

              var player_div = document.createElement("div");
              var node = document.createTextNode(answer['player']['name']);
              player_div.className = 'player submitter';
              player_div.style.backgroundColor = answer['player']['color'];
              player_div.appendChild(node);
              answer_div.appendChild(player_div);

              var node = document.createTextNode(answer['score']);
              var score_div = document.createElement("div");
              score_div.className = 'score';
              score_div.appendChild(node);
              answer_div.appendChild(score_div);

              var node = document.createTextNode(answer['answer']);
              var answerVal_div = document.createElement("div");
              answerVal_div.className = 'answer';
              answerVal_div.appendChild(node);
              answer_div.appendChild(answerVal_div);

              var votes_div = document.createElement("div");
              votes_div.className = 'votes';
              
              answer['votes'].forEach(function(vote) {
                var node = document.createTextNode(vote['value']);
                var vote_div = document.createElement("div");
                vote_div.style.backgroundColor = vote['color'];
                vote_div.className = 'vote player';
                vote_div.appendChild(node);
                
                
                votes_div.appendChild(vote_div);
              });
              answer_div.appendChild(votes_div);
              answer_div_wrap.appendChild(answer_div);
              div.appendChild(answer_div_wrap);
            });

            var node = document.createTextNode(json['info']['question']);
            var question_div = document.createElement("div");
            question_div.className = 'question';
            question_div.appendChild(node);

            element.appendChild(question_div);
            element.appendChild(div);
          } 
          document.getElementById("results-box").style.display = 'block';
        }
      }
    }

    //Deprecated: ajax
    //window.setTimeout(getInfo, 1000);
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

function esc(html) {
  var text = document.createTextNode(html);
  var div = document.createElement('div');
  div.appendChild(text);
  return div.innerHTML;
}