//-------- Load all the basic commande and the basic info -------

let header = document.createElement("header");

let buttonCostVariable = document.createElement("a");
buttonCostVariable.innerText = "Coût Variable";
buttonCostVariable.onclick = switchToCostVariable;
buttonCostVariable.setAttribute("class", "menuButton");

let buttonCostFixed = document.createElement("a");
buttonCostFixed.innerText = "Coût Fixe";
buttonCostFixed.onclick = switchToCostFixed;
buttonCostFixed.setAttribute("class", "menuButton");

let buttonRecipe = document.createElement("a");
buttonRecipe.innerText = "Recette";
buttonRecipe.onclick = switchToRecipe;
buttonRecipe.setAttribute("class", "menuButton");

let buttonStat = document.createElement("a");
buttonStat.innerText = "Statistique";
buttonStat.onclick = switchToStat;
buttonStat.setAttribute("class", "menuButton");

let dropDownMenu = document.createElement("div");
dropDownMenu.setAttribute("class", "dropdown");

let dropDownButton = document.createElement("div");
dropDownButton.innerText = "Localisation";
dropDownButton.setAttribute("class", "menuButton");
dropDownButton.onclick = loadLocationInAdder;

let content = document.createElement("div");
content.setAttribute("class", "content");
content.setAttribute("id", "content");

dropDownMenu.append(dropDownButton);
dropDownMenu.append(content);

let buttonLogOut = document.createElement("a");
buttonLogOut.innerText = "Déconnexion";
buttonLogOut.onclick = logOut;
buttonLogOut.setAttribute("class", "menuButton");

let helpButton = document.createElement("a");
helpButton.innerText = "Aide";
helpButton.onclick = helpPage;
helpButton.setAttribute("class", "menuButton");

header.append(buttonCostVariable);
header.append(buttonCostFixed);
header.append(buttonRecipe);
header.append(buttonStat);
header.append(dropDownMenu);
header.append(buttonLogOut);
header.append(helpButton);
document.body.prepend(header);

//setup the location adder form

let locationAdder = document.createElement("div");
locationAdder.setAttribute("class", "list adder");
locationAdder.setAttribute("id", "locationAdder");

let nameImput = document.createElement("input");
nameImput.setAttribute("class", "items-adder");
nameImput.setAttribute("placeholder", "Nom");
nameImput.setAttribute("id", "locationName");
locationAdder.append(nameImput);

let locationAdderButton = document.createElement("Button");
locationAdderButton.setAttribute("class", "buttonAdder");
locationAdderButton.innerHTML = "Enregistrer Localisation";
locationAdderButton.onclick = sendLocationClick;
locationAdder.append(locationAdderButton);

let locationDeleteButton = document.createElement("Button");
locationDeleteButton.setAttribute("class", "buttonAdder");
locationDeleteButton.setAttribute("id", "deleteLocationButton");
locationDeleteButton.innerHTML = "Supprimer Localisation";
locationDeleteButton.onclick = deleteLocation;
locationAdder.append(locationDeleteButton);

let closeButton = document.createElement("Button");
closeButton.setAttribute("class", "buttonAdder");
closeButton.innerHTML = "Fermer";
closeButton.onclick = locationAdderHide;
locationAdder.append(closeButton);

document.body.append(locationAdder);

let locations = null;

let locationID = null;

let isNewLocationAdded = false;

/**
 * switch to the cost variable page
 */
function switchToCostVariable() {
  window.location.href = "/costVariable/index.html";
}

/**
 * switch to the cost fixed page
 */
function switchToCostFixed() {
  window.location.href = "/costFixed/index.html";
}

/**
 * switch to the recipe page
 */
function switchToRecipe() {
  window.location.href = "/recipe/index.html";
}

/**
 * switch to the stats page
 */
function switchToStat() {
  window.location.href = "/stat/index.html";
}

/**
 * switch to the load page
 */
function logOut() {
  fetch(getAuthUrl(), {
    method: "DELETE",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => {
    if (res.ok) {
      window.location.href = "/login/index.html";
    }
  });
}
function helpPage() {
  window.location.href = "/help/index.html";
}
/**
 * return an Int ready to be send to the server
 * @param {*} units
 * @param {*} cents
 * @returns
 */
function jointNumber(units, cents) {
  if (units === "") {
    units = 0;
  }
  cents = cents + "00";
  cents = cents.substring(0, 2);

  return units + cents;
}

/**
 * split into to number, ready to be load in the page
 * @param {*} number
 * @returns
 */
function splitNumber(number) {
  number = `${number}`;
  if (number.length <= 2) {
    number = "00" + number;
  }
  let numberUnit = number.substring(0, number.length - 2);
  let numberCents = number.substring(number.length - 2, number.length);

  if (numberUnit == 0) {
    numberUnit = 0;
  }

  return [numberUnit, numberCents];
}

/**
 * load dropdown menu from DB
 */
function loadLocations() {
  fetch(getLocationUrl(), {
    method: "GET",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((res) => {
      if (res.ok) {
        return res.json();
      }
    })
    .then((data) => {
      locations = data;
      loadLocationID();
    });
}
function loadLocationID() {
  fetch(getLocationUrl() + "/id", {
    method: "GET",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((res) => {
      if (res.ok) {
        return res.json();
      }
    })
    .then((data) => {
      locationID = data;
      loadDropDown();
    });
}

function loadDropDown() {
  for (let i = 0; i < locations.length; i++) {
    let item = document.createElement("a");
    item.setAttribute("class", "menuButton drop");
    item.innerHTML = locations[i].name;
    item.setAttribute("id", "l" + locations[i].id);
    item.onclick = switchLocation;
    content.appendChild(item);
  }

  document
    .getElementById("l" + locationID)
    .setAttribute("style", "background-color: #5099c3;");

  let addLocation = document.createElement("a");
  addLocation.setAttribute("class", "menuButton drop");
  addLocation.innerHTML = "+";
  addLocation.onclick = loadNewLocationInAdder;

  content.appendChild(addLocation);
}

//Show or Hide location adder window
function locationAdderShow() {
  document
    .getElementById("locationAdder")
    .setAttribute("style", "display : flex;");
}
function locationAdderHide() {
  document
    .getElementById("locationAdder")
    .setAttribute("style", "display : none;");
}

//select the function to execute in function of the origin of the request (modify or add)
function sendLocationClick() {
  let name = document.getElementById("locationName").value;
  if (name == "") return;
  if (isNewLocationAdded) {
    addNewLocation(name);
  } else {
    modifyOldLocation(name);
  }
}

//add new location
function addNewLocation(name) {
  fetch(getLocationUrl() + "/" + name, {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => {
    if (res.ok) {
      location.reload();
    }
  });
}
//modify old location in DB
function modifyOldLocation(name) {
  fetch(getLocationUrl() + "/" + name, {
    method: "PUT",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => {
    if (res.ok) {
      location.reload();
    }
  });
}
//delete location in DB
function deleteLocation() {
  fetch(getLocationUrl(), {
    method: "DELETE",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => {
    if (res.ok) {
      location.reload();
    }
  });
}

//setup adder Window for modify old one
function loadLocationInAdder() {
  document.getElementById("locationName").value = locations[locationID].name;
  isNewLocationAdded = false;
  document
    .getElementById("deleteLocationButton")
    .setAttribute("style", "display:inline;");
  locationAdderShow();
}

//Setup adder Window for new location
function loadNewLocationInAdder() {
  document.getElementById("locationName").value = "";
  isNewLocationAdded = true;
  document
    .getElementById("deleteLocationButton")
    .setAttribute("style", "display:none;");
  locationAdderShow();
}

//switch location
function switchLocation(event) {
  let id = event.target.id;
  id = id.substring(1);
  fetch(getLocationUrl() + "/user/" + id, {
    method: "PUT",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => {
    if (res.ok) {
      location.reload();
    }
  });
}

loadLocations();
