# Architecture

:heavy_check_mark:_(COMMENT) Add a description of the architecture of your application and create a diagram like the one below. Link to the diagram in this document._

![architectureJAVA drawio (3) (1)](https://github.com/pxlit-projects/project-LennertLinsen/assets/95055204/0551be68-8a2b-40fe-b354-c109efa34583)

sycnhroon: Als de klant een recensie plaatst bij een product met een post-request gebeurt dit asynchroon. De post-message wordt dan op de eventbus gezet, zodat de recensie bij het juiste product in productcatalogus wordt geplaatst.

asynchroon: Als de klant een item toevoegt aan de winkelwagen met een post-request gebeurt dit asynchroon. De post-message wordt dan op de eventbus gezet en dan wordt de stock vermindert van het item.


[Source](https://docs.microsoft.com/en-us/dotnet/architecture/cloud-native/introduce-eshoponcontainers-reference-app)
