<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Diagrama de Gantt</title>

  <style>
    :root {
      --grid: #d9d9d9;
      --august: #b7b7b7;
      --september: #efc1c1;
      --task: #fff800;
      --text: #111;
      --left-title: 190px;
      --owner: 190px;
      --session: 96px;
      --row-h: 31px;
    }

    * {
      box-sizing: border-box;
    }

    body {
      margin: 0;
      padding: 24px;
      font-family: Arial, Helvetica, sans-serif;
      background: white;
      color: var(--text);
    }

    .gantt {
      display: grid;
      grid-template-columns:
        var(--left-title)
        var(--owner)
        repeat(8, var(--session));
      grid-auto-rows: var(--row-h);
      width: max-content;
      border-top: 1px solid var(--grid);
      border-left: 1px solid var(--grid);
      font-size: 14px;
    }

    .cell {
      display: flex;
      align-items: center;
      border-right: 1px solid var(--grid);
      border-bottom: 1px solid var(--grid);
      padding: 0 6px;
      background: #fff;
    }

    .center {
      justify-content: center;
      text-align: center;
    }

    .month {
      font-weight: 600;
      justify-content: flex-start;
      padding-left: 6px;
    }

    .august {
      background: var(--august);
    }

    .september {
      background: var(--september);
    }

    .label {
      font-weight: 500;
    }

    .task {
      background: var(--task);
    }

    /* Hace que se vea bien en pantallas pequeñas */
    .wrapper {
      overflow-x: auto;
      max-width: 100%;
      padding-bottom: 8px;
    }
  </style>
</head>

<body>
  <div class="wrapper">
    <div class="gantt">

      <!-- FILA 1: meses -->
      <div class="cell"></div>
      <div class="cell"></div>

      <div class="cell month august" style="grid-column: 3 / span 4;">
        agosto
      </div>

      <div class="cell month september" style="grid-column: 7 / span 4;">
        septiembre
      </div>

      <!-- FILA 2 -->
      <div class="cell"></div>
      <div class="cell">sesiones</div>
      <div class="cell center"></div>
      <div class="cell center"></div>
      <div class="cell center"></div>
      <div class="cell center"></div>
      <div class="cell center"></div>
      <div class="cell center"></div>
      <div class="cell center"></div>
      <div class="cell center"></div>

      <!-- FILA 3 -->
      <div class="cell"></div>
      <div class="cell">encargados</div>
      <div class="cell center">1</div>
      <div class="cell center">2</div>
      <div class="cell center">3</div>
      <div class="cell center">4</div>
      <div class="cell center">5</div>
      <div class="cell center">6</div>
      <div class="cell center">7</div>
      <div class="cell center">8</div>

      <!-- FILA 4 -->
      <div class="cell label">1. Concepto</div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>

      <!-- 1.1 -->
      <div class="cell label">1.1 Definición de problemática</div>
      <div class="cell"></div>
      <div class="cell task"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>

      <!-- 1.2 -->
      <div class="cell label">1.2 Búsqueda de literatura</div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell task"></div>
      <div class="cell task"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>

      <!-- 1.3 -->
      <div class="cell label">1.3 Estado del arte</div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell task"></div>
      <div class="cell task"></div>
      <div class="cell task"></div>
      <div class="cell task"></div>
      <div class="cell"></div>
      <div class="cell"></div>

      <!-- 1.4 -->
      <div class="cell label">1.4 Lista de exigencias</div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell task"></div>
      <div class="cell task"></div>
      <div class="cell"></div>
      <div class="cell"></div>

      <!-- 1.5 -->
      <div class="cell label">1.5 BlackBox</div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell task"></div>
      <div class="cell task"></div>
      <div class="cell"></div>

      <!-- 1.6 -->
      <div class="cell label">1.6 Estructura de funciones</div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell task"></div>
      <div class="cell task"></div>

      <!-- 1.7 -->
      <div class="cell label">1.7 Secuencia de operaciones</div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell task"></div>
      <div class="cell task"></div>

      <!-- 1.8 -->
      <div class="cell label">1.8 Diagrama de gantt</div>
      <div class="cell center">todos</div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell"></div>
      <div class="cell task"></div>

    </div>
  </div>
</body>
</html>
