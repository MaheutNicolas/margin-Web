let costList = null;
let isCostBeingModify = false;

/**
 * fetch the list of variable cost from the API
 */
function loadFromAPI() {
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
 * Load the page with the list of cost
 */
function loadPage() {
  document.getElementById("costVariableList").innerHTML = "";

  let newItem = document.createElement("div");
  newItem.setAttribute("class", "items");
  newItem.innerText = "Nouveau Coût";
  newItem.onclick = newCost;

  document.getElementById("costVariableList").appendChild(newItem);

  for (let i = 0; i < costList.length; i++) {
    let amount = costList[i].amount;
    let [amountUnit, amountCents] = splitNumber(amount);

    let item = document.createElement("div");
    item.setAttribute("class", "items");
    item.setAttribute("id", costList[i].id);

    let nameLabel = document.createElement("label");
    nameLabel.innerText = costList[i].name + "   ";
    nameLabel.setAttribute("for", costList[i].id);

    let costLabel = document.createElement("label");
    costLabel.innerText = amountUnit + "," + amountCents;
    costLabel.setAttribute("for", costList[i].id);

    item.appendChild(nameLabel);
    item.appendChild(costLabel);
    item.onclick = modifyCost;

    document.getElementById("costVariableList").appendChild(item);
  }
  //reset all
  newCost();
}
/**
 * fetch the id of the button and populate the imput with the correct info, ready to be modified before send
 * @param event : the event link to the button pressed
 */
function modifyCost(event) {
  isCostBeingModify = true;
  document.getElementById("deleteButton").disabled = false;
  let id = event.target.id;
  if (id == "") {
    id = event.target.htmlFor;
  }
  for (let i = 0; i < costList.length; i++) {
    if (costList[i].id == id) {
      document.getElementById("name").value = costList[i].name;

      let amount = costList[i].amount;
      let [amountUnit, amountCents] = splitNumber(amount);

      document.getElementById("amount").value = amountUnit;
      document.getElementById("amountCents").value = amountCents;
      document
        .getElementsByClassName("buttonAdder")[0]
        .setAttribute("id", costList[i].id);
      return;
    }
  }
}
/**
 * reset the value of the imput
 */
function newCost() {
  document.getElementById("name").value = "";
  document.getElementById("amount").value = "";
  document.getElementById("amountCents").value = "";
  isCostBeingModify = false;
  document.getElementById("deleteButton").disabled = true;
}
