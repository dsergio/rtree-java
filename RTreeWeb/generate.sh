npx @openapitools/openapi-generator-cli generate \
  -i http://localhost:8080/v2/api-docs \
  -g typescript-fetch \
  -o ./src/generated/TypeScriptClient \
#   --additional-properties=packageName=MyApp.Client,generateDto=true
