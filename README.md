# myweather
Muestra la previsión de tiempo para los póximos 7 días en la localización actual. Además se pueden añadir otras localizaciones a una lista para ver la previsión de tiempo en las otras localizaciones.
La información del tiempo y localizaciones se obtiene de https://openweathermap.org/
Requiere GooglePlayServices para obtener la ubicación actual.

Modularización por capas (Presentación(UI), Framework, Data, UseCases, Domain)
Capa de Presentación (UI): Aquitectura con patrón de presentación MVVM de Arquitecture Components con DataBinding. Unidireccional Data Flow con StateFlow. MainSate como StateHolder en la capa de UI.
Capa de Framework: Obtención la información de tiempo utilizando el cliente HTTP Retrofit. Carga de imágenes con Glide. Manejo de fragments con Jetpack Navigation y SafeArgs. Inyección de dependencias con Hilt. 
Capa de Data: Define los DataSources y Repositories para acceder los datos locales y remotos. Utiliza Room para almacenar datos localmente. 
Capa de UseCases: Define los casos de uso de la app.
Capa de Domain: Define las entidades de datos que componen la app (Weather, DomainLocation, Error)

Aplicados conceptos de Clean Arquitecture y Principios SOLID.
