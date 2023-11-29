/**
 * fetch the value of the imput and give them to the correct function, depending if the cost is new or not
 */
function addCost() {
  let name = document.getElementById("name").value;
  let amountUnity = document.getElementById("amount").value;
  let amountCents = document.getElementById("amountCents").value;

  let amount = jointNumber(amountUnity, amountCents);

  if (!isCostBeingModify) {
    addNewCost(name, amount);
  } else {
    putExistingCost(name, amount);
  }
}

/**
 * send the new cost to the API
 * @param {*} name
 * @param {*} amount
 */
function addNewCost(name, amount) {
  fetch(getCostUrl(), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({
      name: name,
      amount: amount,
      variable: false,
    }),
  }).then((res) => {
    if (res.ok) {
      loadFromAPI();
    } else {
      alert("Le coût n'a pas été enregistré");
    }
  });
}

/**
 * Modify the currents cost
 * @param {*} name
 * @param {*} amount
 */
function putExistingCost(name, amount) {
  let id = document.getElementsByClassName("buttonAdder")[0].id;
  fetch(getCostUrl(), {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({
      name: name,
      amount: amount,
      variable: false,
      id: id,
    }),
  }).then((res) => {
    if (res.ok) {
      loadFromAPI();
    } else {
      alert("Le coût n'a pas été modifié");
    }
  });
}
