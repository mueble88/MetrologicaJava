INSERT INTO metrologica.user (id, created_at, date_of_birth, email, last_name, last_updated_at, name, password, phone) VALUES (2, 1686602689, '1994-10-30 19:00:00', 'jandresjc@gmail.com', 'Jaramillo', 1686602689, 'Andres', '{bcrypt}$2a$10$x8VH3TR/U/HR2pV61pc4z.cXu6Z4tMiMghlAlLJrFwEiq4q6BW8mW', '111');

INSERT INTO metrologica.user (created_at, date_of_birth, email, last_name, last_updated_at, name, password, phone) VALUES (1689172099, '1994-11-19 19:00:00', 'Ingeniarcorp@gmail.com', '1', 1689172099, 'Ingeniero', '{bcrypt}$2a$10$BaynDSzhrvWcU16M6MJGr.iZFRXeLcvlPACkuT/nC2KlFIkrGOhLy', '11111');
INSERT INTO metrologica.user (created_at, date_of_birth, email, last_name, last_updated_at, name, password, phone) VALUES (1689172221, '1994-11-19 19:00:00', 'Ingeniarcorp2@gmail.com', '2', 1689172221, 'Ingeniero', '{bcrypt}$2a$10$rUunR17EHf.neIBZpzF1WeLXIIiJhixg0Rij9rumLQFMs3EkX6A4q', '22222');
INSERT INTO metrologica.user (created_at, date_of_birth, email, last_name, last_updated_at, name, password, phone) VALUES (1689172250, '1994-11-19 19:00:00', 'Ingeniarcorp3@gmail.com', '3', 1689172250, 'Ingeniero', '{bcrypt}$2a$10$ojWCt43.pNZaewT1O3bJmuhlFlxrHUsfDdWcdn0U.D2KmIiVItTL6', '33333');
INSERT INTO metrologica.user (created_at, date_of_birth, email, last_name, last_updated_at, name, password, phone) VALUES (1689172308, '1994-11-19 19:00:00', 'Ingeniarcorp31@gmail.com', '4', 1689172308, 'Ingeniero', '{bcrypt}$2a$10$ld44iUTwCB/YsriZRIj4Hez6syvkmczw/EdKdvyKOmWHG.zVKa.oq', '44444');

INSERT INTO metrologica.city (id, name) VALUES (1, 'Bogotá');
INSERT INTO metrologica.city (id, name) VALUES (2, 'Medellín');
INSERT INTO metrologica.city (id, name) VALUES (3, 'Istmina');
INSERT INTO metrologica.city (id, name) VALUES (4, 'Santafé De Antioquia');
INSERT INTO metrologica.city (id, name) VALUES (5, 'Montería');
INSERT INTO metrologica.city (id, name) VALUES (6, 'La Estrella');
INSERT INTO metrologica.city (id, name) VALUES (7, 'Envigado');
INSERT INTO metrologica.city (id, name) VALUES (8, 'Jardín');
INSERT INTO metrologica.city (id, name) VALUES (9, 'Itagui');

INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (5, 'Cr 52 #7-115', 'dentalespertos@hotmail.com', 'Yenny Yohana Madrigal', '1036601181', 2, '6042036673');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (6, 'Cr 48 #19a-40 interior 1512', 'njaramillo@gmail.com', 'Nicolás Jaramillo', '70545141', 2, '6044082257000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (7, 'Calle 79c #37-35 int 131', 'admin@ingenieriametrologica.com', 'John Jairo Cárdenas', '71388120', 2, '0313122571236');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (8, 'Calle 72 #12-65', 'contacto@omnitempus.com', 'Omnitempus Limitada', '800106962', 1, '6016110529');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (9, 'Cr 46b #77 sur 36', 'archivos@hospitalsabaneta.gov.co', 'ESE Hospital Venancio Díaz de Sabaneta', '800123106', 2, '0342889701');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (10, 'Cr 50 #52-50', 'info@unionplaza.com.co', 'Centro Comercial Unión Plaza P.H', '800134211', 2, '3013092');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (11, 'Calle 2 sur 46-55 Cl 120', 'claudia.carvajal@oralaser.com.co', 'Unidad Estomologica Las Vegas S.A', '800166631', 2, '6044440840');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (12, 'Cr 42 Cl 3 sur 81 Of 1519-1520', 'yuliana.carmona@coopsana.com.co', 'Cooperativa Antioqueña de Salud Coopsana', '800168083', 2, '0344440055');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (13, 'Cr 76a #32a-73', 'facturaelectronica@itm.edu.co', 'Instituto Tecnológico Metropolitano', '800214750', 2, '60446007275509');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (14, 'Calle 49 #50-21 piso 11 y 12 Ed del café', 'maryluz@gna.org.co', 'Fundación Universidad de Antioquia', '811004659', 2, '0645122060103');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (15, 'Cl 16a sur 32b-38', 'informacion@campestredrivein.com', 'Centro Comercial Campestre Drive P.H', '811006259', 2, '0003005303630000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (16, 'Cr 50 #50-15 Of 317', 'dentotal@une.net.co', 'Dentotal', '811007847', 2, '6044442105');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (17, 'Diagonal 75b #8-40 Of 312', 'factura@geneticaselecta.com.co', 'Genética Selecta SA', '8110158612', 2, '6045574995');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (18, 'Calle 78b #75-21', 'cardiovidfe@vid.org.co', 'Clínica Cardiovid', '811046900', 2, '0345406827');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (19, 'Cr 6 #28a-29', 'cmcubis@gmail.com', 'Centro Médico Cubis LTDA', '818002571', null, '6046700106');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (20, 'Calle 10 #3-24', 'subgefinanciera.hdea@gmail.com', 'E.S.E Hospital San Juan de Dios Santa Fe de Antioquia', '890982264', null, '03430138098');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (21, 'Cr 14 #22-200', 'abo3.juridica@esesanjeronimo.gov.co', 'E.S.E Hospital San Jeronimo de Monteria', '891079999', 15, '47894698');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (22, 'Tv 18b #20-32 Brr las delicias', 'compras@aprehsiltda.com', 'Atención Prehospitalaría Y Seguridad Industrial-Aprehsi LTDA', '900033859', 2, '033503334865');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (23, 'Cr 66b #34a-76 Of 401', 'info@clinicadentalhome.net', 'Dental home SAS', '900050381', 2, '0003104194654000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (24, 'Cr 69f Bis 1-03 p 2', 'inbiocol2006@yahoo.com', 'IB Inbiocol De Colombia limitada Ingenieria Y Técnologia Biomedica A Su Servicio', '900089990', 1, '180117101');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (25, 'Cr 52 #42-60 Lc 107', 'compras@hospimedicos.com', 'Hospimedicos Medellín SA', '9001001759', 2, '0342326018');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (26, 'Calle 34 #63a-93', 'catalinamcorreac@gmail.com', 'IPS Dermolaser LTDA', '900220866', 2, '0343221529');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (27, 'Calle 16 #41-210 Of 606', 'cad@valorar.com', 'Avaluos Y Tasaciones De Colombia Valorar S.A.', '900232534', 2, '0344480727');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (28, 'Cr 48 #32b sur 30', 'gerencia@clinicanova.com.co', 'Clínica Plastica Y Estetica Nova S.A.S', '900326211', 2, '6046046233000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (29, 'Av Cl 80 #69-70bg 36', 'alearcolombia@gmail.com', 'Alear Colombia S.A.S', '900346539', 1, '6047561643000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (30, 'Calle 118 bis #11d-51', 'andresmunetones@gmail.com', 'Ultra Schall de Colombia SAS', '9004143766', 1, '03437793301009');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (31, 'Calle 9a #75-65 Lc 101', 'mercasalud@hotmail.com', 'Mercasalud Sur SAS', '900438878', 2, '0344089061');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (32, 'Calle 39b # 18a-11', 'factura.electronica@clinicos.com.co', 'Clinicos Programas de Atención Integral S.A.S.IPS', '900496641', 1, '03301521753');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (33, 'Cr 55 #79b sur 40', 'administracion@bioximad.com.co', 'Bioximad SAS', '900511866', null, '0344444892');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (34, 'Cr 52 #77b 64 In 104', 'atiempoensalud@gmail.com', 'A Tiempo En Salud', '900529876', 2, '30047914');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (35, 'Calle 19a #44-25 Con 2001', 'facturasoral@gmail.com', 'OralStudio', '900697724', 2, '6043229191');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (36, 'Calle 49 sur 43a 26 In 101', 'auxiliaradministrativa@dentomat.com.co', 'Dentomat SAS', '900784477', 28, '604');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (37, 'Calle 3 sur #51b-38', 'administracion@inmavecolombia.com', 'Inmave Colombia', '900887221', 2, '3174033702');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (38, 'Tv 39b #70-53 Of 202', 'comercial.espencasa@gmail.com', 'Especialistas En Casa Medicina Domiciliaria SAS', '901154675', 2, '033148909570');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (39, 'Calle 62 #35-43 Of 304', 'gerencia@innovid.com.co', 'Innovid SAS', '901347556', 1, '033108601404');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (40, 'Cr 65 #40-25', 'valeria02180@gmail.com', 'Respira Salud SAS', '901434599', 2, '000321781746000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (41, 'Cr 43a #39a-128', 'asistirbiomedica@gmail.com', 'Asistir Biomedica SAS', '901489177', 2, '033217864738');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (42, 'Mall Belen Av. Calle 20a #76-33', 'master.dental2022@gmail.com', 'Dental Máster', '901581931', 2, '60430454362');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (43, 'Calle 76gg #83a-14', 'biomedicals.mantenimiento@gmail.com', 'Biomedicals', '98711412', 2, '6043185311102');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (44, 'Calle 47 #79-33', 'info@bioalma.com', 'Alejandro Lainez', '10171181435', 2, '5731936828');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (45, 'Cr 70 #30-62 ', 'comegasonrisas@gmail.com', 'Lina Marcela Díez', '1017131024', 2, '00032164095000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (46, 'Cr 46 #49a-27 Cons 417', 'erikajgarciag@gmail.com', 'Erika Johana García', '1053807553', 2, '00032072814000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (47, 'No aplica', 'gracejohana28@hotmail.com', 'Greys Johanna Rubiano Paloma', '1110465585', null, '6043173491914000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (48, 'No aplica', 'odontologiadenticare@gmail.com', 'Andrea Hoyos', '1128436484', 2, '0003013969675000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (49, 'Cr 45 #79-11', 'farma.ahorrosla45@gmail.com', 'Naida Lizet Cartagena', '1128439504', 2, '0003218507508000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (50, 'Calle 7 #39-107 Cons 1103', 'info@alvarocorreaj.com', 'Alvaro Corre ', '17150412', 2, '0003052648938000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (51, 'Calle 100 # 88-71', 'rth198306@gmail.com', 'Ruth Elena Garcia Valderrama', '39425731', 2, '00031365531000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (52, 'Cr 73a #30b-70', 'isabeltorodontologia@gmail.com', 'Isabel Toro', '432218226', 2, '6043003054861000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (53, 'Cr 73a #30b-70', 'mariaisabelgranada@gmail.com', 'María Isabel Granada Toro', '43221836', 2, '6046063316000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (54, 'Cr 77a #49b-28', 'unioral@hotmail.com', 'Natalia Cristina Ramirez Osorio', '43611168', 2, '00031172462000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (55, 'Cr 43 #33-57 San Diego 135', 'smilesandiego@hotmail.com', 'Sebastián Tamayo Vásquez', '43630444', 2, '30175815');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (56, 'Calle 59 # 51d-41', 'lingmontoya@gmail.com', 'Lina Montoya ', '43738410', 2, '0000000000000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (57, 'No aplica', 'orthofaceitagui@gmail.com', 'Claudia Barrantes', '43873042', 26, '6040000000000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (58, 'Calle 20 sur #27-55 local 9963', 'santiagobarrientosr2@une.net.co', 'Santiago Barrientos Restrepo', '70112238', 2, '60431643077');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (59, 'No aplica', 'luisffa01@gmail.com', 'Luis Fernando Florez Arredondo', '71278928', 2, '0003104148527000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (60, 'Calle 20 #76-33 l 212', 'inprodingenieria@gmail.com', 'Juan carlos Mesa Rodríguez', '71365558', 2, '0003188371305000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (61, 'Cr 86 #64-97', 'francohenaohectorjaime@gmail.com', 'Jaime Franco Henao', '71611072', 2, '00031479302000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (62, 'Torre Fundadores Cons 1106', 'vitalki.odontologia@gmail.com', 'Marcel Goez Giraldo', '71746327', 2, '6043042419366000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (63, 'Calle 2 aa #59-09', 'susmedicos@une.net.co', 'Sus Medicos SAS', '800113024', 2, '6044486363');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (64, 'Cr 42 #3sur 81 Of 1519', 'radicacionfacturas@coopsana.com.co', 'Cooperativa Antioqueña De Salud', '800168083', 2, '6044440055000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (65, 'Robledo', 'biomeda829@gmail.com', 'Juan Chica', '8129135', 2, '3148356202');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (66, 'Calle 2 sur #4655', 'felipefranco@une.net.co', 'Felipe Franco', '8395209', 2, '60440885850');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (67, 'Cr 24 #13-466', 'contratacionesyolombo@gmail.com', 'E.S.E San Rafael de Yolombo', '890981536', 2, '6048654859');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (68, 'Cr 75 #30a-19', 'calidad@centrocita.org', 'Corporación Centro Cita- Salud Mental', '900081843', 2, '6045946808000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (69, 'Trv A #45-46 Patio Bonito-Poblado', 'facturacion@hospimedicos.com', 'Hospimedicos', '900101759', 2, '000312297894000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (70, 'Calle 11a sur #44-23', 'informacion@quirustetic.com', 'Quirustetic Sociedad Por Acciones Simplificada', '900223000', 2, '6043214383');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (71, 'Calle 50 #81a-23', 'farmaciamedicannips@gmail.com', 'Medicann Pharma', '901212253', 2, '0003006466450000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (72, 'Trv Cr25 #4-165 Local 98-34', 'maralodontologia@gmail.com', 'Maral Company SAS', '9015506243', 2, '0003136084773000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (73, 'Calle 1a #54-34', 'pajonc111@hotmail.com', 'Carlos Pajón ', '98547264', 2, '6043625525');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (74, 'C.C profecional el crucero Cr 48 #12sur 70 cons 301', 'consultorio.andresduque@gmail.com', 'Andrés Duque Acevedo', '98547384', 2, '00031257369000');
INSERT INTO metrologica.client (id, address, email, name, nit, city_id, phone) VALUES (75, 'No aplica', 'biomedicals.mantenimiento@gmail.com', 'John Pacheco', '98711412', 2, '6043185311102000');
