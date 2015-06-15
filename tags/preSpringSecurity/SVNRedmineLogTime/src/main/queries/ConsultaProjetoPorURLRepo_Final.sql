 select 
     p.id, 
     p.name, 
     r.id repository_id, 
     r.url, 
     r.root_url 
 from  
  repositories r 
 inner join projects p on 
     p.id = r.id  
 where   
     instr(concat(r.root_url,'/','jazzxdoclet/'),r.url)  
 order by  
     length(url) desc









select 
    
    p.id            ,
    p.name          ,
    r.id repository_id,
    r.url,
    r.root_url
from 
    repositories r
inner join projects p on
    p.id = r.id
where 
    instr(concat(r.root_url,'/jazzxdoclet'),r.url)
order by
    length(url) desc




select 
    
*
from 
    repositories r
where 
    instr(concat(r.root_url,'/jazzxdoclet/subprojeto1/'),r.url)
order by
    length(url) desc