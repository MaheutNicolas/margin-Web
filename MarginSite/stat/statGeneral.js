/**
 * Format the text with all info and print it in the viewer
 */
function viewGeneralStat() {
  document.getElementById("statViewer").innerHTML = "";
  let label = document.createElement("label");
  let [incomeUnits, incomeCents] = splitNumber(stats.incomeThreshold);
  let [ticketUnits, ticketCents] = splitNumber(stats.averageTicket);
  let [totalFixedUnits, totalFixedCents] = splitNumber(stats.fixedCostTotal);

  let text = `Classement meilleurs marges :
   `;
  for (let i = 0; i < stats.marginRanking.length; i++) {
    text =
      text +
      `${i + 1}. ${stats.marginRanking[i].name}
    `;
  }
  text =
    text +
    `
    Classement meilleurs marges en % :
   `;
  for (let i = 0; i < stats.percentRanking.length; i++) {
    text =
      text +
      `${i + 1}. ${stats.percentRanking[i].name}
    `;
  }

  text =
    text +
    `
  Analyse charges Fixes :
  Marge moyen des recettes : ${stats.marginVariableAverage}%
  Coût total fixe : ${totalFixedUnits},${totalFixedCents}
  seuil de rentabilité : ${incomeUnits},${incomeCents}
  ticket moyen : ${ticketUnits},${ticketCents}
  Nombre de ventes pour seuil : ${stats.sellToThreshold}`;

  label.innerText = text;
  document.getElementById("statViewer").appendChild(label);
}
