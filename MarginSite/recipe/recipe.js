let recipeList = null;
let costList = null;
let isRecipeBeingModify = false;

/**
 * fetch the list of recipe from the API
 */
function loadFromAPI() {
  fetch(getRecipeUrl(), {
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
      recipeList = data;
      loadCost();
    });
}
/**
 * fetch the list of variable cost from the API
 */
function loadCost() {
  fetch(getCostVariableUrl(), {
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
      costList = data;
      loadPage();
    });
}

/**
 * Load the page with the list of recipe
 */
function loadPage() {
  document.getElementById("recipeList").innerHTML = "";

  let newItem = document.createElement("div");
  newItem.setAttribute("class", "items");
  newItem.innerText = "Nouvelle Recette";
  newItem.onclick = newRecipe;

  document.getElementById("recipeList").appendChild(newItem);

  for (let i = 0; i < recipeList.length; i++) {
    let price = recipeList[i].price;

    let [priceUnits, priceCents] = splitNumber(price);

    let item = document.createElement("div");
    item.setAttribute("class", "items");
    item.setAttribute("id", recipeList[i].id);

    let nameLabel = document.createElement("label");
    nameLabel.innerText = recipeList[i].name + "   ";
    nameLabel.setAttribute("for", recipeList[i].id);

    let priceLabel = document.createElement("label");
    priceLabel.innerText = priceUnits + "," + priceCents;
    priceLabel.setAttribute("for", recipeList[i].id);

    item.appendChild(nameLabel);
    item.appendChild(priceLabel);
    item.onclick = modifyRecipe;

    document.getElementById("recipeList").appendChild(item);
  }

  //add radio Button Cost

  document.getElementById("costRadioButton").innerHTML = "Liste des Coûts :";

  for (let i = 0; i < costList.length; i++) {
    let item = document.createElement("div");

    let button = document.createElement("input");
    button.setAttribute("type", "checkbox");
    button.name = "cost";
    button.value = costList[i].id;
    button.id = "cost" + costList[i].id;

    let label = document.createElement("label");
    label.innerText = costList[i].name;
    label.setAttribute("for", "cost" + costList[i].id);

    item.appendChild(button);
    item.appendChild(label);

    document.getElementById("costRadioButton").appendChild(item);
  }
  //resetAll
  newRecipe();
}

/**
 * fetch the id of the button and populate the imput with the correct info, ready to be modified before send
 * @param event : the event link to the button pressed
 */
function modifyRecipe(event) {
  newRecipe();
  isRecipeBeingModify = true;
  document.getElementById("deleteButton").disabled = false;
  let id = event.target.id;
  if (id == "") {
    id = event.target.htmlFor;
  }
  for (let i = 0; i < recipeList.length; i++) {
    if (recipeList[i].id == id) {
      document.getElementById("name").value = recipeList[i].name;
      let price = recipeList[i].price;
      let [priceUnits, priceCents] = splitNumber(price);

      let tax = recipeList[i].tax;
      let [taxUnits, taxCents] = splitNumber(tax);

      document.getElementById("price").value = priceUnits;
      document.getElementById("priceCents").value = priceCents;
      document.getElementById("tax").value = taxUnits;
      document.getElementById("taxCents").value = taxCents;
      document
        .getElementsByClassName("buttonAdder")[0]
        .setAttribute("id", recipeList[i].id);

      let costs = recipeList[i].costs;
      let costBox = document.getElementsByName("cost");
      for (let i = 0; i < costs.length; i++) {
        for (let j = 0; j < costBox.length; j++) {
          if (costs[i].id == costBox[j].value) {
            costBox[j].checked = true;
          }
        }
      }
      return;
    }
  }
}
/**
 * reset the value of the imput
 */
function newRecipe() {
  document.getElementById("name").value = "";
  document.getElementById("price").value = "";
  document.getElementById("priceCents").value = "";
  document.getElementById("tax").value = "";
  document.getElementById("taxCents").value = "";
  isRecipeBeingModify = false;
  document.getElementById("deleteButton").disabled = true;

  let costBox = document.getElementsByName("cost");
  for (let i = 0; i < costBox.length; i++) {
    costBox[i].checked = false;
  }
}
