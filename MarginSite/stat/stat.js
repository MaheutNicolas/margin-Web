let recipeList = null;
let fixedCostList = null;
let stats = null;

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
      loadFixedCost();
    });
}
/**
 * fetch the list of fixed cost from the API
 */
function loadFixedCost() {
  fetch(getCosFixedUrl(), {
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
      fixedCostList = data;
      loadStats();
    });
}
/**
 * fetch the stats from the API
 */
function loadStats() {
  fetch(getStatUrl(), {
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
      stats = data;
      loadPage();
    });
}
/**
 * Load the page with the list of stats and recipe
 */
function loadPage() {
  let generalButton = document.createElement("div");
  generalButton.setAttribute("class", "items");
  generalButton.innerText = "Statistique Générale";
  generalButton.onclick = viewGeneralStat;
  document.getElementById("statList").append(generalButton);

  for (let i = 0; i < recipeList.length; i++) {
    let button = document.createElement("div");
    button.setAttribute("class", "items");
    button.innerText = recipeList[i].name;
    button.setAttribute("id", recipeList[i].id);
    button.onclick = openRecipePage;
    document.getElementById("statList").append(button);
  }
  viewGeneralStat();
}
