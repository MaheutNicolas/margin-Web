/**
 * fetch the value of the imput and give them to the correct function, depending if the recipe is new or not
 */
function addRecipe() {
  let name = document.getElementById("name").value;
  let priceUnits = document.getElementById("price").value;
  let priceCents = document.getElementById("priceCents").value;
  let taxUnits = document.getElementById("tax").value;
  let taxCents = document.getElementById("taxCents").value;

  let price = jointNumber(priceUnits, priceCents);
  let tax = jointNumber(taxUnits, taxCents);

  let costs = [];
  let costBox = document.getElementsByName("cost");
  for (let i = 0; i < costBox.length; i++) {
    if (costBox[i].checked === true) {
      costs.push(costBox[i].value);
    }
  }

  if (!isRecipeBeingModify) {
    addNewRecipe(name, price, tax, costs);
  } else {
    putExistingRecipe(name, price, tax, costs);
  }
}
/**
 * send the new recipe to the API
 * @param {*} name
 * @param {*} amount
 */
function addNewRecipe(name, price, tax, costs) {
  fetch(getRecipeUrl(), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({
      name: name,
      price: price,
      tax: tax,
      costs: costs,
    }),
  }).then((res) => {
    if (res.ok) {
      loadFromAPI();
    } else {
      alert("La recette n'a pas été enregistrée");
    }
  });
}
/**
 * Modify the currents recipe
 * @param {*} name
 * @param {*} amount
 */
function putExistingRecipe(name, price, tax, costs) {
  let id = document.getElementsByClassName("buttonAdder")[0].id;
  fetch(getRecipeUrl(), {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({
      name: name,
      price: price,
      tax: tax,
      costs: costs,
      id: id,
    }),
  }).then((res) => {
    if (res.ok) {
      loadFromAPI();
    } else {
      alert("Le recette n'a pas été modifiée");
    }
  });
}
