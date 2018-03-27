--Q1
select c.cname from customer c, buyvehicle b
where c.SSN=b.SSN and b.byear=2005; 

--Q2
select distinct c.cname from customer c
where exists (select * from buyvehicle b, vehicle v
			  where b.vin=v.vin and b.SSN=c.SSN and v.vmaker='Honda' 
              and b.price > 20000 and v.vyear>=2005 and v.vyear<=2010);
--Q3
select distinct c.cname from customer c
where NOT exists(select * from buyvehicle b
				 where b.SSN=c.SSN);
    
--Q4
select distinct j1.SSN as SSN1, j2.SSN as SSN2 from 
(select b.SSN,v.vmaker,v.vmodel,v.vyear from buyvehicle b, vehicle v where b.vin = v.vin) j1,
(select b.SSN,v.vmaker,v.vmodel,v.vyear from buyvehicle b, vehicle v where b.vin = v.vin) j2
where j1.vmaker=j2.vmaker and j1.vmodel=j2.vmodel and j1.vyear=j2.vyear and j1.ssn<j2.ssn;

--Q5
select SSN from 
(select distinct c.SSN,count(distinct v.vmodel) as nmodel from customer c, buyvehicle b, vehicle v where c.SSN=b.SSN and b.vin = v.vin and c.cgender='Female' and v.vmaker='Nissan') j1,
(select count(distinct v.vmodel) as nmodel from vehicle v where v.vmaker = 'Nissan') j2
where j1.nmodel=j2.nmodel;

--Q6
select cars.vmaker,cars.vmodel from 
(select vmaker,vmodel, count(*) as c from 
vehicle v join buyvehicle b on v.vin=b.vin and b.price>=10000
group by vmaker,vmodel) cars,
(select vmaker, vmodel, count(*) as c from 
vehicle group by vmaker,vmodel) bars
where cars.vmaker=bars.vmaker and cars.vmodel=bars.vmodel and cars.c=bars.c;

--Q7
select vmaker,vmodel as count from
vehicle natural join buyvehicle j
group by vmaker, vmodel
having count(*)=(select max(count) from
				(select vmaker,vmodel,count(*) as count from
				vehicle natural join buyvehicle j
				group by vmaker, vmodel) jar); 
    
--Q8
select mars.vin, mars.vmodel, mars.vmaker, cars.price from
(select vmaker, max(price) as price from
buyvehicle natural join vehicle
group by vmaker) cars,
(select v.vin, b.price, v.vmaker,v.vmodel from buyvehicle b join vehicle v on v.vin=b.vin) mars 
where mars.vmaker=cars.vmaker and mars.price=cars.price;

--Q9
select vmaker, sum(price) from
(select ssn from buyvehicle 
group by ssn
having count(*)>=3) as cus,
(select b.SSN,v.vin, b.price, v.vmaker,v.vmodel from buyvehicle b join vehicle v Using(vin)) bv
where bv.ssn=cus.ssn
group by vmaker;

--Q10
select vmaker, avg(price) from 
vehicle natural join buyvehicle
group by vmaker;

