function testMapNegative() returns (string){
    map animals;
    animals = {"animal1":"Lion", "animal2":"Cat", "animal3":"Leopard", "animal4":"Dog"};
    string value = "aString";
    value = animals["animal1"];
    return value;
}
