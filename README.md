# Calcontrol

<img height="150" src="https://user-images.githubusercontent.com/117776720/204882501-084343dd-ad7f-4c0e-aa76-6f2c03d1d5d0.png" />

Calcontrol is an application for managing and tracking daily caloric intake and expenditure.  

It gives user high level of customization because it tailors caloric expenditure on user's individual characteristics instead of just generalizing user to some predefined group (exact logic and steps to determining that is too broad for this form so if you want to know more how it is imagined and implemented check out my [baccalaureus graduate thesis](https://github.com/msarDevelopment/Calcontrol/files/10127630/1351_Miro_Saric_0296012718.pdf), especially chapters 5 - 7).  

:warning: Work in progress, currently in MVP state to showcase how it works as a system, designed UI is not yet implemented :warning:

<br/><br/>

Central point of the application is Dashboard, where user can see relation of caloric intake and expenditure for a specific day.

<p>
  <img height="500" src="https://user-images.githubusercontent.com/117776720/204136757-58a5dd68-d2cf-4b14-8899-ed2903b276d5.jpg" />
  <img height="500" src="https://user-images.githubusercontent.com/117776720/204136788-64e43fcf-674e-4dc7-b70e-4f5cb76a3627.jpg" />
  <img height="500" src="https://user-images.githubusercontent.com/117776720/204136808-dd05a9d8-e581-400e-be73-87def8a939ec.jpg" />
</p>

<br/><br/>

First Activity shown is an Activity for Plan selection where user can create new Plan or select some Plan user created earlier (Plan consists of Days which consist of Meals and Workouts which represent caloric intake and expenditure respectively).

| Creating new Plan  | Plan concept |
| ------------- | ------------- |
| <video src="https://user-images.githubusercontent.com/117776720/204136972-1a1b6e76-0999-4634-9142-00290f2c29c2.mp4">  | <img src="https://user-images.githubusercontent.com/117776720/204137020-544a4693-d2f3-4864-a410-5d8d0f3b817f.png" />
  
<br/><br/>

After selecting Plan, Activity which represents that Plan is shown and to add calories to intake and expenditure for each Day in that Plan, user first needs to create some Meals and Workouts.

| Adding to caloric intake  | Adding to caloric expenditure |
| ------------- | ------------- |
| <video src="https://user-images.githubusercontent.com/117776720/204137239-dfc022eb-6100-4dbe-999e-d7ba12057a22.mp4">  | <video src="https://user-images.githubusercontent.com/117776720/204137261-e86746d9-01f5-4468-bc4c-7c34f2eb614e.mp4">

<br/><br/>

Meals and Workouts consist of smaller units, Ingredients and Exercises (Meal consists of one or more Ingredients and Workout consists of one or more Exercises) and custom-made backend provides those smaller units.
  
<img height="500" src="https://user-images.githubusercontent.com/117776720/204138011-e508e7bb-8c5e-49eb-815f-d253bd821a6e.png" />
  
<br/><br/>
  
That backend is made of database (which provides hundreds of Ingredients and Exercises) and PHP scripts which take query parameter from routes and return JSON list of desired objects whose name contains that query parameter.
- Ingredients are reachable via (example for "banana") http://www.fitness.testingonly.gq/ingredients/search/?name=banana
- Exercises are reachable via (example for "volleyball") http://www.fitness.testingonly.gq/exercises/search/?name=volleyball
  
<table>

<tr>
<td> Example of PHP script (for Ingredients) </td> <td> Response for ingredients/search/?name=banana </td> <td> Response for exercises/search/?name=volleyball </td>
</tr>

<tr>
<td>

```php
$query="SELECT * FROM ingredient WHERE ingredient_name LIKE ?;";

    $search_name = htmlspecialchars($_GET["name"]);

    $likeVar = "%" . $search_name . "%";

    $ingredient_list = array();

    $stmt=mysqli_stmt_init($dbc);
    if (mysqli_stmt_prepare($stmt, $query)){
        mysqli_stmt_bind_param($stmt,"s", $likeVar);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_store_result($stmt);
    }
    mysqli_stmt_bind_result($stmt, $id, $ingredient_name,
    $measurement_amount, $measurement_unit, $energy_kcal,
    $fat, $carbohydrate, $sugar, $dietary_fiber, $protein);
    
    while (mysqli_stmt_fetch($stmt)) {
        $ingredient = new Ingredient($id, $ingredient_name,
        $measurement_amount, $measurement_unit, $energy_kcal,
        $fat, $carbohydrate, $sugar, $dietary_fiber, $protein);
        array_push($ingredient_list, $ingredient);
    }

    mysqli_close($dbc);

    header('Content-Type: application/json; charset=utf-8');

    echo json_encode($ingredient_list);
```

</td>

<td>

```json
[
  {
    "id": 34,
    "ingredient_name": "Banana, dried",
    "measurement_amount": 100,
    "measurement_unit": "g",
    "energy_kcal": 273,
    "fat": 1,
    "carbohydrate": 60.2,
    "sugar": 43.7,
    "dietary_fiber": 5.5,
    "protein": 3
  },
  {
    "id": 35,
    "ingredient_name": "Banana, dried (banana chips)",
    "measurement_amount": 100,
    "measurement_unit": "g",
    "energy_kcal": 370,
    "fat": 1.8,
    "carbohydrate": 80.7,
    "sugar": 65.3,
    "dietary_fiber": 7.6,
    "protein": 3.9
  },
  {
    "id": 36,
    "ingredient_name": "Banana, raw",
    "measurement_amount": 100,
    "measurement_unit": "g",
    "energy_kcal": 95,
    "fat": 0.3,
    "carbohydrate": 21,
    "sugar": 17.2,
    "dietary_fiber": 2,
    "protein": 1.1
  }
]
```

</td>

<td>

```json
[
  {
    "id": 254,
    "exercise_name": "volleyball",
    "met_value": 4
  },
  {
    "id": 255,
    "exercise_name": "volleyball, competitive, in gymnasium",
    "met_value": 6
  },
  {
    "id": 256,
    "exercise_name": "volleyball, non-competitive, 6 – 9 member team, general",
    "met_value": 3
  },
  {
    "id": 257,
    "exercise_name": "volleyball, beach, in sand",
    "met_value": 8
  },
  {
    "id": 339,
    "exercise_name": "water volleyball",
    "met_value": 3
  }
]
```

</td>

</tr>

</table>

<br/><br/>

Locally, all entities described earlier are saved and managed in database using the following schema.

<img src="https://user-images.githubusercontent.com/117776720/204139461-5fd00a11-03cc-48dc-925a-eeb8c1f12fcb.png" />

<br/><br/>

Application follows MVVM architecture (LiveData, Repository with Retrofit and Room), its structure can be represented with the following visualization.

<img src="https://user-images.githubusercontent.com/117776720/204139558-b8d6abf4-e4cb-4000-9d8b-4b622c4beada.jpg" /> 
