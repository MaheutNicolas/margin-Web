/**
 * load all the info of the recipe and print it in the viewer
 * @param {*} event
 */
function openRecipePage(event) {
  document.getElementById("statViewer").innerHTML = "";
  let recipe = recipeList[event.target.id];
  let label = document.createElement("label");
  let [priceUnits, priceCents] = splitNumber(recipe.price);
  let [taxUnits, taxCents] = splitNumber(recipe.tax);
  let [priceTaxLessUnits, priceTaxLessCents] = splitNumber(
    recipe.priceWithoutTax
  );
  let marge = recipe.priceWithoutTax - recipe.totalCost;
  let [margeUnits, margeCents] = splitNumber(marge);
  let [totalCostUnits, totalCostCents] = splitNumber(recipe.totalCost);
  let [middleCostUnits, middleCostCents] = splitNumber(recipe.middleCost);
  let [totalFixedUnits, totalFixedCents] = splitNumber(stats.fixedCostTotal);

  let numberOfSaleToFixed = Math.ceil(stats.fixedCostTotal / marge);

  let costArrayDiv = document.createElement("div");
  costArrayDiv.setAttribute("id", "costArrayDiv");

  let nameDiv = document.createElement("div");
  nameDiv.setAttribute("class", "arrayDiv");
  let nameLabel = document.createElement("label");
  nameLabel.innerHTML = "Nom";
  nameDiv.appendChild(nameLabel);

  let amountDiv = document.createElement("div");
  amountDiv.setAttribute("class", "arrayDiv");
  let amountLabel = document.createElement("label");
  amountLabel.innerHTML = "Montant";
  amountDiv.appendChild(amountLabel);

  let percentDiv = document.createElement("div");
  percentDiv.setAttribute("class", "arrayDiv");
  let percentLabel = document.createElement("label");
  percentLabel.innerHTML = "Pourcentage";
  percentDiv.appendChild(percentLabel);

  let toCostAverageDIv = document.createElement("div");
  toCostAverageDIv.setAttribute("class", "arrayDiv");
  let toCostAverageLabel = document.createElement("label");
  toCostAverageLabel.innerHTML = "sur Moyenne";
  toCostAverageDIv.appendChild(toCostAverageLabel);

  for (let i = 0; i < recipe.costs.length; i++) {
    let [costUnits, costCents] = splitNumber(recipe.costs[i].amount);
    let [ToMiddleUnits, toMiddleCents] = splitNumber(
      recipe.costs[i].amount - recipe.middleCost
    );

    let nameLabel = document.createElement("label");
    nameLabel.innerHTML = recipe.costs[i].name;
    nameDiv.appendChild(nameLabel);

    let amountLabel = document.createElement("label");
    amountLabel.innerHTML = costUnits + "," + costCents;
    amountDiv.appendChild(amountLabel);

    let percentLabel = document.createElement("label");
    percentLabel.innerHTML = recipe.costs[i].percent + "%";
    percentDiv.appendChild(percentLabel);

    let toCostAverageLabel = document.createElement("label");
    toCostAverageLabel.innerHTML = ToMiddleUnits + "," + toMiddleCents;
    toCostAverageDIv.appendChild(toCostAverageLabel);
  }

  costArrayDiv.appendChild(nameDiv);
  costArrayDiv.appendChild(amountDiv);
  costArrayDiv.appendChild(percentDiv);
  costArrayDiv.appendChild(toCostAverageDIv);

  let text = `Nom : ${recipe.name} 
    prix : ${priceUnits},${priceCents}  /  TVA : ${taxUnits},${taxCents}
    prix sans TVA : ${priceTaxLessUnits},${priceTaxLessCents}  /  pourcentage : 100 %
    coût total variable : ${totalCostUnits},${totalCostCents}  /  pourcentage : ${
    100 - recipe.marginPercent
  }%
    marge variable : ${margeUnits},${margeCents}  /  pourcentage : ${
    recipe.marginPercent
  }%
    
    nombres de coût variable : ${recipe.costs.length}
    moyen des coûts : ${middleCostUnits},${middleCostCents}
  
    Coût total fixe : ${totalFixedUnits},${totalFixedCents}
    nombre de coûts fixes : ${fixedCostList.length}
    
    Nombre de ventes pour coût fixe : ${numberOfSaleToFixed}
    
    `;

  label.innerText = text;
  document
    .getElementById("statViewer")
    .append(label, document.createElement("br"), costArrayDiv);
}
