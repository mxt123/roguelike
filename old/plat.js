
var actorChars = {
  "@": Player,
};

function Player(pos) {
  this.pos = pos.plus(new Vector(0, -0.5));
  this.size = new Vector(0.8, 0.8);
  this.speed = new Vector(0, 0);
}
Player.prototype.type = "player";

function countAliveNeighbours (grid,x,y){
  var count = 0;
  for (i= -1;i<2;i++) {
    for (j= -1;j<2;j++){
      var checkX = x+j;
      var checkY = y+i;  
      if (checkX == x && checkY ==y) {
        // do nothing
      } else if (checkY <= 0 || checkX <= 0 || checkY >= grid.length || checkX >= grid[0].length) {
          count +=1;
      }
      else if (grid[checkY][checkX] == 'wall') {
         count += 1;
      } 
    }
  }
  return count;
}

var changeToStartAlive = 0.4;//0.45;

function evolveGrid(grid){
  var newgrid = grid.slice();
  for (var y = 0; y < grid.length ; y++) {
    for (var x = 0; x < grid[0].length ; x++) {    
      var count = countAliveNeighbours(grid,x,y);
      var isAlive = grid[y][x] === "wall";
      if (isAlive) {
        if ( count < 3) {
          newgrid[y][x] = null;
        } 
        else {
          newgrid[y][x] = "wall";
        } 
      }
       else if (!isAlive) {
        if (count > 4 ){
          newgrid[y][x] = "wall";
        } else {
          newgrid[y][x] = null;
        }
      }

    }
  }
  grid = newgrid;
  newgrid=undefined;   
}

function generateGrid(grid){  
  for (var y = 0; y < grid.length ; y++) {
    for (var x = 0; x < grid[0].length ; x++) {    
        if (  Math.random() < changeToStartAlive) {
          grid[y][x] = "wall";
        } else {
          grid[y][x] = null;  
        }
      }
    }
}

function Level(plan) {
  this.width = plan[0].length;
  this.height = plan.length;
  this.grid = [];
  this.actors = [];
  for (var y = 0; y < this.height; y++) {
    var line = plan[y], gridLine = [];
    for (var x = 0; x < this.width; x++) {
      var ch = line[x], fieldType = null;
      var Actor = actorChars[ch];
      if (Actor)
        this.actors.push(new Actor(new Vector(x, y), ch));
      else if (ch == "x")
        fieldType = "wall";      
      gridLine.push(fieldType);
    }
    this.grid.push(gridLine);
  }

  this.player = this.actors.filter(function(actor) {
    return actor.type == "player";
  })[0];
  this.status = this.finishDelay = null;
  generateGrid(this.grid);
  for ( var count =0 ; count < 6; count++) {
     evolveGrid(this.grid)
  }

}

Level.prototype.isFinished = function() {
  return this.status != null && this.finishDelay < 0;
};

function elt(name, className) {
  var elt = document.createElement(name);
  if (className) elt.className = className;
  return elt;
}

var scale = 20;

function Vector(x, y) {
  this.x = x; this.y = y;
}
Vector.prototype.plus = function(other) {
  return new Vector(this.x + other.x, this.y + other.y);
};
Vector.prototype.times = function(factor) {
  return new Vector(this.x * factor, this.y * factor);
};

Level.prototype.obstacleAt = function(pos, size) {
  var xStart = Math.floor(pos.x);
  var xEnd = Math.ceil(pos.x + size.x);
  var yStart = Math.floor(pos.y);
  var yEnd = Math.ceil(pos.y + size.y);

  if (xStart < 0 || xEnd > this.width || yStart < 0)
    return "wall";
  for (var y = yStart; y < yEnd; y++) {
    for (var x = xStart; x < xEnd; x++) {
      var fieldType = this.grid[y][x];
      if (fieldType) return fieldType;
    }
  }
};

Level.prototype.actorAt = function(actor) {
  for (var i = 0; i < this.actors.length; i++) {
    var other = this.actors[i];
    if (other != actor &&
        actor.pos.x + actor.size.x > other.pos.x &&
        actor.pos.x < other.pos.x + other.size.x &&
        actor.pos.y + actor.size.y > other.pos.y &&
        actor.pos.y < other.pos.y + other.size.y)
      return other;
  }
};

var maxStep = 0.05;

Level.prototype.animate = function(step, keys) {
  if (this.status != null)
    this.finishDelay -= step;

  while (step > 0) {
    var thisStep = Math.min(step, maxStep);
    this.actors.forEach(function(actor) {
      actor.act(thisStep, this, keys);
    }, this);
    step -= thisStep;
  }
};

var playerXSpeed = 7;

Player.prototype.move = function(step, level, keys) {
  this.speed.x = 0;
  this.speed.y = 0;
  if (keys.left) this.speed.x -= playerXSpeed;
  if (keys.right) this.speed.x += playerXSpeed;
  if (keys.down) this.speed.y += playerXSpeed;
  if (keys.up) this.speed.y -= playerXSpeed;

  var xmotion = new Vector(0, this.speed.y * step);
  var ymotion = new Vector(this.speed.x * step, 0);

  var obstaclex = level.obstacleAt(this.pos.plus(xmotion), this.size);
  var obstacley = level.obstacleAt(this.pos.plus(ymotion), this.size);

  if (obstaclex && obstacley) {
    react = level.playerTouched(obstaclex,null);
    if (react) {
      var bounce = new Vector(-this.speed.x * (step*2), -this.speed.y * (step * 2));

       this.pos = this.pos.plus(bounce);
    
    }
  } else if(obstaclex) {
    level.playerTouched(obstaclex,null);
    this.pos = this.pos.plus(ymotion);
  } else if(obstacley) {
    level.playerTouched(obstaclex,null)
    this.pos = this.pos.plus(xmotion);   
  }else {
    this.pos = this.pos.plus(xmotion);
    this.pos = this.pos.plus(ymotion);
  }
};

Player.prototype.act = function(step, level, keys) {
  this.move(step, level, keys);

  var otherActor = level.actorAt(this);
  if (otherActor)
    level.playerTouched(otherActor.type, otherActor);

  // Losing animation
  if (level.status == "lost") {
    this.pos.y += step;
    this.size.y -= step;
  }
};

Level.prototype.playerTouched = function(type, actor) {
  if (type == "wall") {
      return true;
    }
};

var arrowCodes = {37: "left", 38: "up", 39: "right", 40: "down"};

function trackKeys(codes) {
  var pressed = Object.create(null);
  function handler(event) {
    if (codes.hasOwnProperty(event.keyCode)) {
      var down = event.type == "keydown";
      pressed[codes[event.keyCode]] = down;
      event.preventDefault();
    }
  }
  addEventListener("keydown", handler);
  addEventListener("keyup", handler);
  return pressed;
}

function runAnimation(frameFunc) {
  var lastTime = null;
  function frame(time) {
    var stop = false;
    if (lastTime != null) {
      var timeStep = Math.min(time - lastTime, 100) / 1000;
      stop = frameFunc(timeStep) === false;
    }
    lastTime = time;
    if (!stop)
      requestAnimationFrame(frame);
  }
  requestAnimationFrame(frame);
}

var arrows = trackKeys(arrowCodes);

function runLevel(level, Display, andThen) {
  var display = new Display(document.body, level);
  runAnimation(function(step) {
    level.animate(step, arrows);
    display.drawFrame(step);
    if (level.isFinished()) {
      display.clear();
      if (andThen)
        andThen(level.status);
      return false;
    }
  });
}

function runGame(plans, Display) {
  function startLevel(n) {
    runLevel(new Level(plans[n]), Display, function(status) {
      if (status == "lost")
        startLevel(n);
      else if (n < plans.length - 1)
        startLevel(n + 1);
      else
        console.log("You win!");
    });
  }
  startLevel(0);
}