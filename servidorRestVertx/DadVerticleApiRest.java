package acuario;


import acuario.Sensores;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.asyncsql.MySQLClient;

public class DadVerticleApiRest extends AbstractVerticle {

	//private Map<Integer, DomoState> elements = new LinkedHashMap<>();

	private static AsyncSQLClient asynSQLClient;
	
	@Override
	public void start(Future<Void> startFuture) {
		JsonObject config = new JsonObject().put("host", "localhost").put("username","root").put("password","root")
				.put("database","dadrest").put("port", 3306);
				
		Router router = Router.router(vertx);
		asynSQLClient = MySQLClient.createShared(getVertx(), config);
		vertx.createHttpServer().requestHandler(router::accept).listen(8080, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});
		router.route("/api/sensores*").handler(BodyHandler.create());
		router.get("/api/sensores").handler(this::getAll);
		router.put("/api/sensores").handler(this::ModificarOne);
		router.get("/api/sensores/:nombre").handler(this::getPorNombre);
	}
	private void getAll(RoutingContext routingContext) {
		asynSQLClient.getConnection(conn -> {
			if(conn.succeeded()){
				try{
				conn.result().queryWithParams("SELECT * FROM sensores;", new JsonArray(), res ->{
					if (res.succeeded()){
						ResultSet resultSet = res.result();
						routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
															.end(Json.encodePrettily(resultSet.getRows()));
					}else{
						routingContext.response()
							.setStatusCode(400)
							.end();
					}
				});
				} catch (Exception e){
					routingContext.response().setStatusCode(401).end();
				}
			}else{
				routingContext.response()
					.setStatusCode(400)
					.end();
				
			}
		});
		
		
		
	}
	private void getPorNombre(RoutingContext routingContext) {
		asynSQLClient.getConnection(conn -> {
			if(conn.succeeded()){
				try{
					String nombre=routingContext.request().getParam("nombre");
				conn.result().queryWithParams("SELECT * FROM sensores WHERE nombre=?;", new JsonArray().add(nombre), res ->{
					if (res.succeeded()){
						ResultSet resultSet = res.result();
						routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
															.end(Json.encodePrettily(resultSet.getRows()));
					}else{
						routingContext.response()
							.setStatusCode(400)
							.end();
					}
				});
				} catch (Exception e){
					routingContext.response().setStatusCode(401).end();
				}
			}else{
				routingContext.response()
					.setStatusCode(400)
					.end();
				
			}
		});
		
		
		
	}

	private void ModificarOne(RoutingContext routingContext) {
		final Sensores element = Json.decodeValue(routingContext.getBodyAsString(), Sensores.class);
		
	
		asynSQLClient.getConnection(conn -> {
			if(conn.succeeded()){
				try{
				conn.result().queryWithParams("UPDATE sensores SET valor=?,respuesta=? WHERE nombre=?", new JsonArray().add(element.getValor()).add(element.getRespuesta()).add(element.getNombre()), res ->{
					if (res.succeeded()){
						routingContext.response().setStatusCode(200).end();
					}else{
						routingContext.response()
							.setStatusCode(400)
							.end();
					}
				});
				} catch (Exception e){
					routingContext.response().setStatusCode(401).end();
				}
			}else{
				routingContext.response()
					.setStatusCode(400)
					.end();
				
			}
		});
	}
	
	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

}
